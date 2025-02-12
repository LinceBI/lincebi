package com.stratebi.lincebi.permissions.service;

import com.stratebi.lincebi.permissions.model.Role;
import com.stratebi.lincebi.permissions.model.User;
import org.pentaho.platform.api.engine.IUserRoleListService;
import org.pentaho.platform.engine.core.system.PentahoSystem;

import java.util.Comparator;
import java.util.List;

public class PermissionsService {

	private final IUserRoleListService userRoleListService;

	public PermissionsService() {
		this.userRoleListService = PentahoSystem.get(IUserRoleListService.class);
	}

	public User[] getAllUsers() {
		List<String> names = this.userRoleListService.getAllUsers();
		names.sort(Comparator.naturalOrder());
		User[] users = new User[names.size()];
		for (int i = 0; i < names.size(); i++) {
			users[i] = new User();
			users[i].setName(names.get(i));
		}
		return users;
	}

	public Role[] getAllRoles() {
		List<String> names = this.userRoleListService.getAllRoles();
		names.sort(Comparator.naturalOrder());
		Role[] roles = new Role[names.size()];
		for (int i = 0; i < names.size(); i++) {
			roles[i] = new Role();
			roles[i].setName(names.get(i));
		}
		return roles;
	}

	public Role[] getUserRoles(String name) {
		List<String> names = this.userRoleListService.getRolesForUser(null, name);
		names.sort(Comparator.naturalOrder());
		Role[] roles = new Role[names.size()];
		for (int i = 0; i < names.size(); i++) {
			roles[i] = new Role();
			roles[i].setName(names.get(i));
		}
		return roles;
	}

}
