package com.stratebi.lincebi.integration.powerbi.service;

import java.util.Collections;
import java.util.List;

import org.pentaho.platform.api.engine.IAuthorizationPolicy;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.IUserRoleListService;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service with helper methods to get user and roles
 */
public class BIServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BIServerService.class);

	private static final String ANONYMOUS_USER = "anonymousUser";
	private static final String ANONYMOUS_ROLE = "Anonymous";

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
