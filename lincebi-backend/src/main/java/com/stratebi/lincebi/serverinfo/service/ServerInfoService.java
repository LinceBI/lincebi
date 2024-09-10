package com.stratebi.lincebi.serverinfo.service;

import javax.jcr.Repository;
import org.apache.jackrabbit.core.RepositoryImpl;
import com.stratebi.lincebi.serverinfo.model.ServerInfo;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.util.VersionHelper;
import org.pentaho.platform.util.VersionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerInfoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoService.class);

	public static ServerInfo get() {
		ServerInfo serverInfo = new ServerInfo();

		// Get LinceBI version
		serverInfo.setLincebiVersion(ServerInfoService.class.getPackage().getImplementationVersion());

		// Get BI Server version
		serverInfo.setBiserverVersion(VersionHelper.getVersionInfo(PentahoSystem.class).getVersionNumber());

		// Get instance ID
		Repository jcrRepository = PentahoSystem.get(Repository.class, "jcrRepository", null);
		if (jcrRepository != null) {
			RepositoryImpl repository = (RepositoryImpl)jcrRepository;
			serverInfo.setInstanceId(repository.getConfig().getClusterConfig().getId());
		} else {
			LOGGER.error("Cannot obtain JCR repository");
			serverInfo.setInstanceId("Unknown");
		}

		return serverInfo;
	}

}
