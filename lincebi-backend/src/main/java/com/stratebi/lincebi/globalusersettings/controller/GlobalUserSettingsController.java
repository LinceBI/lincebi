package com.stratebi.lincebi.globalusersettings.controller;

import org.codehaus.enunciate.Facet;
import org.pentaho.platform.api.usersettings.IUserSettingService;
import org.pentaho.platform.api.usersettings.pojo.IUserSetting;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.web.http.api.resources.Setting;
import org.pentaho.platform.web.http.api.resources.utils.EscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/lincebi/api/global-user-settings")
@Facet(name = "Unsupported")
public class GlobalUserSettingsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalUserSettingsController.class);

	private static final byte[] LOCK = new byte[0];

	private static IUserSettingService getSettingService() {
		return PentahoSystem.get(IUserSettingService.class, PentahoSessionHolder.getSession());
	}

	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public ArrayList<Setting> listController() {
		IUserSettingService settingsService = GlobalUserSettingsController.getSettingService();
		List<IUserSetting> globalUserSettings = settingsService.getGlobalUserSettings();

		ArrayList<Setting> settings = new ArrayList<>();
		for (IUserSetting globalUserSetting : globalUserSettings) {
			settings.add(new Setting(globalUserSetting.getSettingName(), globalUserSetting.getSettingValue()));
		}

		return settings;
	}

	@GET
	@Path("{setting : .+}")
	@Facet(name = "Unsupported")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getController(
		@PathParam("setting") String setting
	) {
		IUserSettingService settingsService = GlobalUserSettingsController.getSettingService();
		IUserSetting globalUserSetting = settingsService.getGlobalUserSetting(setting, null);

		if (globalUserSetting != null && globalUserSetting.getSettingValue() != null) {
			return Response.ok(globalUserSetting.getSettingValue()).build();
		} else {
			return Response.noContent().build();
		}
	}

	@POST
	@Path("{setting : .+}")
	@Facet(name = "Unsupported")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response setController(
		@PathParam("setting") String setting,
		String settingValue
	) {
		IUserSettingService settingsService = GlobalUserSettingsController.getSettingService();

		synchronized (GlobalUserSettingsController.LOCK) {
			try {
				settingsService.setGlobalUserSetting(setting, EscapeUtils.escapeJsonOrRaw(settingValue));
				return Response.ok(settingValue).build();
			} catch (Exception ex) {
				GlobalUserSettingsController.LOGGER.error(ex.getMessage());
				return Response.serverError().build();
			}
		}
	}

}
