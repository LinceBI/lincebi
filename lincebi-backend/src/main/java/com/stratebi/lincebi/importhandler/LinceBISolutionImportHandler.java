package com.stratebi.lincebi.importhandler;

import org.apache.commons.io.IOUtils;
import org.pentaho.metadata.repository.DomainAlreadyExistsException;
import org.pentaho.metadata.repository.DomainIdNullException;
import org.pentaho.metadata.repository.DomainStorageException;
import org.pentaho.platform.api.mimetype.IMimeType;
import org.pentaho.platform.api.repository2.unified.IPlatformImportBundle;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.repository2.unified.webservices.StringKeyStringValueDto;
import org.pentaho.platform.api.usersettings.IUserSettingService;
import org.pentaho.platform.core.mimetype.MimeType;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.plugin.services.importer.IPlatformImportHandler;
import org.pentaho.platform.plugin.services.importer.IPlatformImporter;
import org.pentaho.platform.plugin.services.importer.PlatformImportException;
import org.pentaho.platform.plugin.services.importer.RepositoryFileImportBundle;
import org.pentaho.platform.plugin.services.importer.SolutionImportHandler;
import org.pentaho.platform.plugin.services.importexport.ExportFileNameEncoder;
import org.pentaho.platform.plugin.services.importexport.ExportManifestUserSetting;
import org.pentaho.platform.plugin.services.importexport.ImportSource.IRepositoryFileBundle;
import org.pentaho.platform.plugin.services.importexport.RepositoryFileBundle;
import org.pentaho.platform.plugin.services.importexport.exportManifest.ExportManifest;
import org.pentaho.platform.repository.RepositoryFilenameUtils;
import org.pentaho.platform.web.http.api.resources.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LinceBISolutionImportHandler extends SolutionImportHandler implements IPlatformImportHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(LinceBISolutionImportHandler.class);

	private static final byte[] LOCK = new byte[0];

	private static final String MANIFEST_FILENAME = "exportManifest.xml";
	private static final Pattern MANIFEST_FILENAME_PATTERN = Pattern.compile("^(?:.*/)?" + Pattern.quote(LinceBISolutionImportHandler.MANIFEST_FILENAME) + "$");

	private static final String LOCALE_EXTENSION = ".locale";
	private static final Pattern LOCALE_EXTENSION_PATTERN = Pattern.compile("^.+" + Pattern.quote(LinceBISolutionImportHandler.LOCALE_EXTENSION) + "$");

	static {
		// Register import handler
		IPlatformImporter platformImporter = PentahoSystem.get(IPlatformImporter.class);
		platformImporter.addHandler(new LinceBISolutionImportHandler());
	}

	private final IUserSettingService settingService;
	private final FileService fileService;

	private ByteArrayOutputStream bundleOutputStream;

	public LinceBISolutionImportHandler() {
		this(Collections.singletonList(
				new MimeType("application/vnd.lincebi.solution-repository", "lbix")
		));
	}

	public LinceBISolutionImportHandler(List<IMimeType> mimeTypes) {
		super(mimeTypes);

		this.settingService = PentahoSystem.get(IUserSettingService.class);
		this.fileService = new FileService();
	}

	@Override
	public void importFile(IPlatformImportBundle bundle) throws PlatformImportException, DomainIdNullException, DomainAlreadyExistsException, DomainStorageException, IOException {
		super.importFile(this.loadBundleInputStream(bundle));

		List<IRepositoryFileBundle> manifestFiles = new ArrayList<>();
		List<IRepositoryFileBundle> localeFiles = new ArrayList<>();

		RepositoryFileImportBundle importBundle = (RepositoryFileImportBundle) bundle;
		String importPath = importBundle.getPath();

		InputStream bundleInputStream = new ByteArrayInputStream(this.bundleOutputStream.toByteArray());

		try (ZipInputStream zipInputStream = new ZipInputStream(bundleInputStream)) {
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {
				String entryName = RepositoryFilenameUtils.separatorsToRepository(entry.getName());
				boolean isManifestFile = !entry.isDirectory() && LinceBISolutionImportHandler.MANIFEST_FILENAME_PATTERN.matcher(entryName).matches();
				boolean isLocaleFile = !entry.isDirectory() && LinceBISolutionImportHandler.LOCALE_EXTENSION_PATTERN.matcher(entryName).matches();

				if (isManifestFile || isLocaleFile) {
					File tempFile = File.createTempFile("zip", null);
					tempFile.deleteOnExit();

					FileOutputStream fos = new FileOutputStream(tempFile);
					IOUtils.copy(zipInputStream, fos);
					fos.close();

					File file = new File(entryName);
					RepositoryFile repoFile = new RepositoryFile.Builder(file.getName()).folder(false).hidden(false).build();

					String parentDir = new File(entryName).getParent() == null
							? RepositoryFile.SEPARATOR
							: new File(entryName).getParent() + RepositoryFile.SEPARATOR;

					IRepositoryFileBundle repoFileBundle = new RepositoryFileBundle(repoFile, null, parentDir, tempFile, "UTF-8", null);
					if (isManifestFile) manifestFiles.add(repoFileBundle);
					else localeFiles.add(repoFileBundle);
				}

				zipInputStream.closeEntry();
			}
		} catch (IOException ex) {
			LinceBISolutionImportHandler.LOGGER.error(ex.getMessage());
			throw new PlatformImportException(ex.getMessage());
		}

		for (IRepositoryFileBundle manifestFile : manifestFiles) {
			ExportManifest manifest;
			try {
				byte[] bytes = IOUtils.toByteArray(manifestFile.getInputStream());
				ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				manifest = ExportManifest.fromXml(in);
			} catch (IOException | JAXBException ex) {
				LinceBISolutionImportHandler.LOGGER.error(ex.getMessage());
				throw new PlatformImportException(ex.getMessage());
			}

			List<ExportManifestUserSetting> globalUserSettings = manifest.getGlobalUserSettings();
			for (ExportManifestUserSetting globalUserSetting : globalUserSettings) {
				synchronized (LinceBISolutionImportHandler.LOCK) {
					this.settingService.setGlobalUserSetting(globalUserSetting.getName(), globalUserSetting.getValue());
				}
			}
		}

		for (IRepositoryFileBundle localeFile : localeFiles) {
			String localeFileDirPath = localeFile.getPath();
			String localeFileName = localeFile.getFile().getName();
			if (manifestFiles.size() > 0) {
				localeFileDirPath = ExportFileNameEncoder.decodeZipFileName(localeFileDirPath);
				localeFileName = ExportFileNameEncoder.decodeZipFileName(localeFileName);
			}
			String localeFilePath = RepositoryFilenameUtils.concat(importPath, RepositoryFilenameUtils.concat(localeFileDirPath, localeFileName));
			String parentFilePath = localeFilePath.substring(0, localeFilePath.length() - LinceBISolutionImportHandler.LOCALE_EXTENSION.length());

			if (this.fileService.getRepository().getFile(parentFilePath) == null) {
				try {
					this.fileService.createFile("UTF-8", parentFilePath, new ByteArrayInputStream(new byte[0]));
				} catch (Exception ex) {
					LinceBISolutionImportHandler.LOGGER.error(ex.getMessage());
					throw new PlatformImportException(ex.getMessage());
				}
			}

			List<StringKeyStringValueDto> localeProperties = new ArrayList<>();
			InputStream inputStream = localeFile.getInputStream();
			PropertyResourceBundle rb = new PropertyResourceBundle(inputStream);
			Enumeration<?> keyEnum = rb.getKeys();
			while (keyEnum.hasMoreElements()) {
				String key = String.valueOf(keyEnum.nextElement());
				String value = String.valueOf(rb.getObject(key));
				localeProperties.add(new StringKeyStringValueDto(key, value));
			}
			inputStream.close();

			try {
				this.fileService.doSetLocaleProperties(parentFilePath, RepositoryFile.DEFAULT_LOCALE, localeProperties);
			} catch (Exception ex) {
				LinceBISolutionImportHandler.LOGGER.error(ex.getMessage());
				throw new PlatformImportException(ex.getMessage());
			}
		}
	}

	private IPlatformImportBundle loadBundleInputStream(IPlatformImportBundle bundle) throws IOException {
		RepositoryFileImportBundle importBundle = (RepositoryFileImportBundle) bundle;
		InputStream bundleInputStream = importBundle.getInputStream();

		this.bundleOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bundleInputStream.read(buffer)) > -1) this.bundleOutputStream.write(buffer, 0, len);
		this.bundleOutputStream.flush();

		bundleInputStream = new ByteArrayInputStream(this.bundleOutputStream.toByteArray());
		importBundle.setInputStream(bundleInputStream);

		return bundle;
	}
}
