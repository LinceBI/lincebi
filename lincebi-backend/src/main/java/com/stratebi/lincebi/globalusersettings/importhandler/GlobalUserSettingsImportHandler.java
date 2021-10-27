package com.stratebi.lincebi.globalusersettings.importhandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.pentaho.platform.api.mimetype.IMimeType;
import org.pentaho.platform.api.repository2.unified.IPlatformImportBundle;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.usersettings.IUserSettingService;
import org.pentaho.platform.core.mimetype.MimeType;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.plugin.services.importer.IPlatformImportHandler;
import org.pentaho.platform.plugin.services.importer.IPlatformImporter;
import org.pentaho.platform.plugin.services.importer.PlatformImportException;
import org.pentaho.platform.plugin.services.importexport.ExportManifestUserSetting;
import org.pentaho.platform.plugin.services.importexport.RepositoryFileBundle;
import org.pentaho.platform.plugin.services.importexport.ImportSource.IRepositoryFileBundle;
import org.pentaho.platform.plugin.services.importexport.exportManifest.ExportManifest;
import org.pentaho.platform.repository.RepositoryFilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalUserSettingsImportHandler implements IPlatformImportHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalUserSettingsImportHandler.class);

	private static final String EXPORT_MANIFEST_FILENAME = "exportManifest.xml";
	private static final Pattern EXPORT_MANIFEST_PATTERN = Pattern.compile("^(?:.*/)?" + Pattern.quote(GlobalUserSettingsImportHandler.EXPORT_MANIFEST_FILENAME) + "$");

	static {
		// Register import handler
		IPlatformImporter platformImporter = PentahoSystem.get(IPlatformImporter.class);
		platformImporter.addHandler(new GlobalUserSettingsImportHandler());
	}

	private IUserSettingService settingService;
	private List<IMimeType> mimeTypes;

	public GlobalUserSettingsImportHandler() {
		this.settingService = PentahoSystem.get(IUserSettingService.class);
		this.mimeTypes = new ArrayList<>();
		this.mimeTypes.add(new MimeType("application/vnd.pentaho.global-user-settings", "pgus"));
	}

	@Override
	public List<IMimeType> getMimeTypes() {
		return this.mimeTypes;
	}

	@Override
	public void importFile(IPlatformImportBundle bundle) throws PlatformImportException, IOException {
		ZipInputStream zipImportStream = new ZipInputStream(bundle.getInputStream());
		GlobalUserSettingsImportSource importSource = new GlobalUserSettingsImportSource(zipImportStream);

		for (IRepositoryFileBundle fileBundle : importSource.getManifestFiles()) {
			ExportManifest manifest = this.getExportManifest(fileBundle);
			if (manifest == null) return;

			List<ExportManifestUserSetting> globalUserSettings = manifest.getGlobalUserSettings();
			for (ExportManifestUserSetting globalUserSetting : globalUserSettings) {
				this.settingService.setGlobalUserSetting(globalUserSetting.getName(), globalUserSetting.getValue());
			}
		}
	}

	private ExportManifest getExportManifest(IRepositoryFileBundle fileBundle) {
		ExportManifest manifest = null;

		try {
			byte[] bytes = IOUtils.toByteArray(fileBundle.getInputStream());
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			manifest = ExportManifest.fromXml(in);
		} catch (IOException | JAXBException ex) {
			GlobalUserSettingsImportHandler.LOGGER.error(ex.getMessage());
		}

		return manifest;
	}

	static class GlobalUserSettingsImportSource {
		private ZipInputStream zipInputStream;
		private List<IRepositoryFileBundle> manifestFiles;

		public GlobalUserSettingsImportSource(final ZipInputStream zipInputStream) {
			this.zipInputStream = zipInputStream;
			this.manifestFiles = new ArrayList<>();
			this.initialize();
		}

		private void initialize() {
			try {
				ZipEntry entry = this.zipInputStream.getNextEntry();

				while (entry != null) {
					String entryName = RepositoryFilenameUtils.separatorsToRepository(entry.getName());

					if (entry.isDirectory() || !GlobalUserSettingsImportHandler.EXPORT_MANIFEST_PATTERN.matcher(entryName).matches()) {
						this.zipInputStream.closeEntry();
						entry = this.zipInputStream.getNextEntry();
						continue;
					}

					File tempFile = File.createTempFile("zip", null);
					tempFile.deleteOnExit();

					FileOutputStream fos = new FileOutputStream(tempFile);
					IOUtils.copy(this.zipInputStream, fos);
					fos.close();

					File file = new File(entryName);
					RepositoryFile repoFile = new RepositoryFile.Builder(file.getName()).folder(false).hidden(false).build();

					String parentDir = new File(entryName).getParent() == null
						? RepositoryFile.SEPARATOR
						: new File(entryName).getParent() + RepositoryFile.SEPARATOR;

					IRepositoryFileBundle repoFileBundle = new RepositoryFileBundle(repoFile, null, parentDir, tempFile, "UTF-8", null);
					this.manifestFiles.add(repoFileBundle);

					this.zipInputStream.closeEntry();
					entry = this.zipInputStream.getNextEntry();
				}

				this.zipInputStream.close();
			} catch (IOException ex) {
				GlobalUserSettingsImportHandler.LOGGER.error(ex.getMessage());
			}
		}

		public List<IRepositoryFileBundle> getManifestFiles() {
			return this.manifestFiles;
		}
	}

}
