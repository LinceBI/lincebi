package com.stratebi.lincebi.integration.powerbi.service;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratebi.lincebi.integration.powerbi.config.PowerBIConfig;

/**
 * Service to authenticate using MSAL
 */
public class AzureADService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AzureADService.class);

	/**
	 * Acquires access token for the based on config values
	 */
	public static String getAccessToken(PowerBIConfig config) throws MalformedURLException, InterruptedException, ExecutionException {
		if (config.authenticationType.equalsIgnoreCase("MasterUser")) {
			return AzureADService.getAccessTokenUsingMasterUser(config);
		} else if (config.authenticationType.equalsIgnoreCase("ServicePrincipal")) {
			return AzureADService.getAccessTokenUsingServicePrincipal(config);
		} else {
			throw new RuntimeException("Invalid authentication type: " + config.authenticationType);
		}
	}

	/**
	 * Acquires access token for the given clientId and app secret
	 */
	public static String getAccessTokenUsingServicePrincipal(PowerBIConfig config) throws MalformedURLException, InterruptedException, ExecutionException {
		// Build Confidential Client App
		ConfidentialClientApplication app = ConfidentialClientApplication.builder(
			config.clientId,
			ClientCredentialFactory.createFromSecret(config.serviceprincipalClientSecret)
		)
			.authority(config.authorityUrl + config.serviceprincipalTenantId)
			.build();

		ClientCredentialParameters clientCreds = ClientCredentialParameters.builder(
			Collections.singleton(config.scopeUrl)
		)
			.build();

		// Acquire new AAD token
		IAuthenticationResult result = app.acquireToken(clientCreds).get();

		// Return access token if token is acquired successfully
		if ((result != null) && (result.accessToken() != null) && !result.accessToken().isEmpty()) {
			AzureADService.LOGGER.debug("Authenticated with ServicePrincipal mode");
			return result.accessToken();
		} else {
			AzureADService.LOGGER.error("Failed to authenticate with ServicePrincipal mode");
			return null;
		}
	}

	/**
	 * Acquires access token for the given clientId and user credentials
	 */
	public static String getAccessTokenUsingMasterUser(PowerBIConfig config) throws MalformedURLException, InterruptedException, ExecutionException {
		// Build Public Client App
		PublicClientApplication app = PublicClientApplication.builder(
			config.clientId
		)
			.authority(config.authorityUrl + "organizations") // Use authorityUrl+tenantId if this doesn't work
			.build();

		UserNamePasswordParameters userCreds = UserNamePasswordParameters.builder(
			Collections.singleton(config.scopeUrl),
			config.masteruserUsername,
			config.masteruserPassword.toCharArray()
		)
			.build();

		// Acquire new AAD token
		IAuthenticationResult result = app.acquireToken(userCreds).get();

		// Return access token if token is acquired successfully
		if ((result != null) && (result.accessToken() != null) && !result.accessToken().isEmpty()) {
			AzureADService.LOGGER.debug("Authenticated with MasterUser mode");
			return result.accessToken();
		} else {
			AzureADService.LOGGER.error("Failed to authenticate with MasterUser mode");
			return null;
		}
	}

}
