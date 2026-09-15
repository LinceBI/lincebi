package com.stratebi.lincebi.integration.powerbi.config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Configuration class
 */
public class PowerBIConfig {

	private static final Map<String, PowerBIConfig> CONFIGS = new HashMap<>();

	// Application id / Client id
	public String clientId = "";

	// Two possible Authentication methods:
	// - For authentication with app secret choose ServicePrincipal as AuthenticationType.
	// - For authentication with master user credential choose MasterUser as AuthenticationType.
	// More details here: https://aka.ms/EmbedServicePrincipal
	public String authenticationType = "ServicePrincipal";

	// Enter ServicePrincipal credentials
	public String serviceprincipalTenantId = "";
	public String serviceprincipalClientSecret = "";

	// Enter MasterUser credentials
	public String masteruserUsername = "";
	public String masteruserPassword = "";

	// Milliseconds the browser waits for a report or dashboard to load before showing an error
	public long loadTimeoutMs = 60 * 1000;

	// DO NOT CHANGE
	public String authorityUrl = "https://login.microsoftonline.com/";
	public String scopeUrl = "https://analysis.windows.net/powerbi/api/.default";

	static {
		Map<String, String> env = System.getenv();

		for (Map.Entry<String, String> var : env.entrySet()) {
			String varKey = var.getKey();
			Pattern varKeyPattern = Pattern.compile("^POWERBI_([A-Z0-9_]+)_CLIENT_ID$");
			Matcher varKeyMatcher = varKeyPattern.matcher(varKey);

			if (varKeyMatcher.find() && varKeyMatcher.groupCount() > 0) {
				String name = varKeyMatcher.group(1);
				PowerBIConfig config = new PowerBIConfig();

				String clientId = env.get("POWERBI_" + name + "_CLIENT_ID");
				if (clientId != null) config.clientId = clientId;

				String authenticationType = env.get("POWERBI_" + name + "_AUTHENTICATION_TYPE");
				if (authenticationType != null) config.authenticationType = authenticationType;

				String serviceprincipalTenantId = env.get("POWERBI_" + name + "_SERVICEPRINCIPAL_TENANT_ID");
				if (serviceprincipalTenantId != null) config.serviceprincipalTenantId = serviceprincipalTenantId;

				String serviceprincipalClientSecret = env.get("POWERBI_" + name + "_SERVICEPRINCIPAL_CLIENT_SECRET");
				if (serviceprincipalClientSecret != null) config.serviceprincipalClientSecret = serviceprincipalClientSecret;

				String masteruserUsername = env.get("POWERBI_" + name + "_MASTERUSER_USERNAME");
				if (masteruserUsername != null) config.masteruserUsername = masteruserUsername;

				String masteruserPassword = env.get("POWERBI_" + name + "_MASTERUSER_PASSWORD");
				if (masteruserPassword != null) config.masteruserPassword = masteruserPassword;

				String authorityUrl = env.get("POWERBI_" + name + "_AUTHORITY_URL");
				if (authorityUrl != null) config.authorityUrl = authorityUrl;

				String scopeUrl = env.get("POWERBI_" + name + "_SCOPE_URL");
				if (scopeUrl != null) config.scopeUrl = scopeUrl;

				String loadTimeoutMs = env.get("POWERBI_" + name + "_LOAD_TIMEOUT_MS");
				if (loadTimeoutMs != null) {
					try {
						long value = Long.parseLong(loadTimeoutMs.trim());
						if (value > 0) config.loadTimeoutMs = value;
					} catch (NumberFormatException ex) {
						// Keep the default load timeout on an invalid value
					}
				}

				PowerBIConfig.put(name.toLowerCase(), config);
			}
		}
	}

	public static PowerBIConfig put(String name, PowerBIConfig value) {
		return PowerBIConfig.CONFIGS.put(name, value);
	}

	public static PowerBIConfig get(String name) {
		return PowerBIConfig.CONFIGS.get(name);
	}

	public static PowerBIConfig remove(String name) {
		return PowerBIConfig.CONFIGS.remove(name);
	}

}
