package com.stratebi.lincebi.lifecyclelistener;

import org.pentaho.platform.api.engine.IPlatformReadyListener;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.pentaho.platform.plugin.services.importer.ArchiveLoader;
import org.pentaho.platform.plugin.services.importer.IPlatformImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.Callable;

public class LifecycleListener implements IPluginLifecycleListener, IPlatformReadyListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleListener.class);

	private static final String DEFAULT_LINCEBI_LOADER_THREAD_NAME = "Default LinceBI Loader Thread";
	// Extracted from: org/pentaho/platform/plugin/action/defaultcontent/DefaultContentSystemListener.java
	private static final String DEFAULT_CONTENT_LOADER_THREAD_NAME = "Default Content Loader Thread";

	private static final String DEFAULT_CONTENT_FOLDER = "system/default-content";

	private static final FilenameFilter FILE_FILTER = (dir, name) -> name.endsWith(".pgus") || name.endsWith(".pfm");

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
		Thread defaultContentLoaderThread = this.getThreadByName(LifecycleListener.DEFAULT_CONTENT_LOADER_THREAD_NAME);

		Runnable runnable = () -> {
			try {
				if (defaultContentLoaderThread != null) {
					// Wait for default content loader thread
					defaultContentLoaderThread.join();
				}

				SecurityHelper.getInstance().runAsSystem((Callable<Void>) () -> {
					String solutionPath = PentahoSystem.getApplicationContext().getSolutionPath(LifecycleListener.DEFAULT_CONTENT_FOLDER);
					File directory = new File(solutionPath);

					IPlatformImporter importer = PentahoSystem.get(IPlatformImporter.class);
					ArchiveLoader archiveLoader = new ArchiveLoader(importer);
					archiveLoader.loadAll(directory, LifecycleListener.FILE_FILTER);

					return null;
				});
			} catch (Exception ex) {
				LifecycleListener.LOGGER.error(ex.getMessage());
			}
		};

		Thread defaultFileMetadataLoaderThread = new Thread(runnable);
		defaultFileMetadataLoaderThread.setName(LifecycleListener.DEFAULT_LINCEBI_LOADER_THREAD_NAME);
		defaultFileMetadataLoaderThread.setDaemon(true);
		defaultFileMetadataLoaderThread.start();
	}

	private Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(threadName)) return t;
		}

		return null;
	}

}
