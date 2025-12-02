package com.stratebi.lincebi.integration.common.service;

import org.pentaho.platform.api.engine.IAuthorizationPolicy;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.IUserRoleListService;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

/**
 * Service with helper methods to get user and roles
 */
public class BIServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BIServerService.class);

	private static final String ANONYMOUS_USER = "anonymousUser";
	private static final String ANONYMOUS_ROLE = "Anonymous";

	private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

	public static String getUser() {
		String user;
		try {
			IPentahoSession session = PentahoSessionHolder.getSession();
			user = session.getName();
		} catch (Throwable ex) {
			BIServerService.LOGGER.error(ex.getMessage());
			user = BIServerService.ANONYMOUS_USER;
		}
		return user;
	}

	public static String getSessionId() {
		String sessionId;
		try {
			IPentahoSession session = PentahoSessionHolder.getSession();
			sessionId = session.getId();
		} catch (Throwable ex) {
			BIServerService.LOGGER.error(ex.getMessage());
			sessionId = "";
		}
		return sessionId;
	}

	public static String getSessionHash() {
		String sessionHash;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(BIServerService.getSessionId().getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder(12);
			for (int i = 0; i < sb.capacity() / 2; i++) {
				sb.append(HEX_CHARS[(hash[i] >> 4) & 0x0F]);
				sb.append(HEX_CHARS[hash[i] & 0x0F]);
			}
			sessionHash = sb.toString();
		} catch (Throwable ex) {
			BIServerService.LOGGER.error(ex.getMessage());
			sessionHash = "e3b0c44298fc";
		}
		return sessionHash;
	}

	public static List<String> getRoles() {
		List<String> roles;
		try {
			IUserRoleListService roleListService = PentahoSystem.get(IUserRoleListService.class);
			roles = roleListService.getRolesForUser(null, BIServerService.getUser());
		} catch (Throwable ex) {
			BIServerService.LOGGER.error(ex.getMessage());
			roles = Collections.singletonList(BIServerService.ANONYMOUS_ROLE);
		}
		return roles;
	}

	public static boolean isAdmin() {
		boolean isAdmin;
		try {
			IAuthorizationPolicy authorizationPolicy = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession());
			isAdmin = authorizationPolicy.isAllowed(AdministerSecurityAction.NAME);
		} catch (Throwable ex) {
			BIServerService.LOGGER.error(ex.getMessage());
			isAdmin = false;
		}
		return isAdmin;
	}

}
