package com.stratebi.lincebi.globalusersettings.lifecyclelistener;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.Callable;

import org.pentaho.platform.api.engine.IPlatformReadyListener;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.pentaho.platform.plugin.services.importer.ArchiveLoader;
import org.pentaho.platform.plugin.services.importer.IPlatformImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalUserSettingsLifecycleListener implements IPluginLifecycleListener, IPlatformReadyListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalUserSettingsLifecycleListener.class);

	private static final String DEFAULT_GLOBAL_USER_SETTINGS_LOADER_THREAD_NAME = "Default Global User Settings Loader Thread";
	// Extracted from: org/pentaho/platform/plugin/action/defaultcontent/DefaultContentSystemListener.java
	private static final String DEFAULT_CONTENT_LOADER_THREAD_NAME = "Default Content Loader Thread";

	private static final String DEFAULT_CONTENT_FOLDER = "system/default-content";

	private static final FilenameFilter FILE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".pgus");
		}
	};

	@Override
	public void init() throws PluginLifecycleException {
	}

	@Override
	public void loaded() throws PluginLifecycleException {
	}

	@Override
	public void unLoaded() throws PluginLifecycleException {
	}

	@Override
	public void ready() throws PluginLifecycleException {
		Thread defaultContentLoaderThread = this.getThreadByName(GlobalUserSettingsLifecycleListener.DEFAULT_CONTENT_LOADER_THREAD_NAME);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					if (defaultContentLoaderThread != null) {
						// Wait for default content loader thread
						defaultContentLoaderThread.join();
					}

					SecurityHelper.getInstance().runAsSystem(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							String solutionPath = PentahoSystem.getApplicationContext().getSolutionPath(GlobalUserSettingsLifecycleListener.DEFAULT_CONTENT_FOLDER);
							File directory = new File(solutionPath);

							IPlatformImporter importer = PentahoSystem.get(IPlatformImporter.class);
							ArchiveLoader archiveLoader = new ArchiveLoader(importer);
							archiveLoader.loadAll(directory, GlobalUserSettingsLifecycleListener.FILE_FILTER);

							return null;
						}
					});
				} catch (Exception ex) {
					GlobalUserSettingsLifecycleListener.LOGGER.error(ex.getMessage());
				}
			}
		};

		Thread defaultGlobalUserSettingsLoaderThread = new Thread(runnable);
		defaultGlobalUserSettingsLoaderThread.setName(GlobalUserSettingsLifecycleListener.DEFAULT_GLOBAL_USER_SETTINGS_LOADER_THREAD_NAME);
		defaultGlobalUserSettingsLoaderThread.setDaemon(true);
		defaultGlobalUserSettingsLoaderThread.start();
	}

	private Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName)) return t;
		}

		return null;
	}

}
