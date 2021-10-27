package com.stratebi.lincebi.filemetadata.importhandler;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.pentaho.platform.api.mimetype.IMimeType;
import org.pentaho.platform.api.repository2.unified.IPlatformImportBundle;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.repository2.unified.webservices.StringKeyStringValueDto;
import org.pentaho.platform.core.mimetype.MimeType;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.plugin.services.importer.IPlatformImportHandler;
import org.pentaho.platform.plugin.services.importer.IPlatformImporter;
import org.pentaho.platform.plugin.services.importer.PlatformImportException;
import org.pentaho.platform.plugin.services.importer.RepositoryFileImportBundle;
import org.pentaho.platform.plugin.services.importexport.ImportSource.IRepositoryFileBundle;
import org.pentaho.platform.plugin.services.importexport.RepositoryFileBundle;
import org.pentaho.platform.repository.RepositoryFilenameUtils;
import org.pentaho.platform.web.http.api.resources.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileMetadataImportHandler implements IPlatformImportHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMetadataImportHandler.class);

	private static final String LOCALE_EXT = ".locale";

	static {
		// Register import handler
		IPlatformImporter platformImporter = PentahoSystem.get(IPlatformImporter.class);
		platformImporter.addHandler(new FileMetadataImportHandler());
	}

	private FileService fileService;
	private List<IMimeType> mimeTypes;

	public FileMetadataImportHandler() {
		this.fileService = new FileService();
		this.mimeTypes = new ArrayList<>();
		this.mimeTypes.add(new MimeType("application/vnd.pentaho.file-metadata", "pfm"));
	}

	@Override
	public List<IMimeType> getMimeTypes() {
		return this.mimeTypes;
	}

	@Override
	public void importFile(IPlatformImportBundle bundle) throws PlatformImportException, IOException {
		RepositoryFileImportBundle importBundle = (RepositoryFileImportBundle) bundle;
		ZipInputStream zipImportStream = new ZipInputStream(bundle.getInputStream());
		FileMetadataImportSource importSource = new FileMetadataImportSource(zipImportStream);

		for (IRepositoryFileBundle fileBundle : importSource.getLocaleFiles()) {
			String parentRepositoryFilePath = this.getParentRepositoryFilePath(importBundle, fileBundle);
			RepositoryFile parentRepositoryFile = this.fileService.getRepository().getFile(parentRepositoryFilePath);

			try {
				if (parentRepositoryFile == null) {
					this.fileService.createFile("UTF-8", parentRepositoryFilePath, new ByteArrayInputStream(new byte[0]));
					parentRepositoryFile = this.fileService.getRepository().getFile(parentRepositoryFilePath);
				}

				List<StringKeyStringValueDto> localeProperties = new ArrayList<>();

				InputStream inputStream = fileBundle.getInputStream();
				PropertyResourceBundle rb = new PropertyResourceBundle(inputStream);
				Enumeration<?> keyEnum = rb.getKeys();
				while (keyEnum.hasMoreElements()) {
					String key = String.valueOf(keyEnum.nextElement());
					String value = String.valueOf(rb.getObject(key));
					localeProperties.add(new StringKeyStringValueDto(key, value));
				}
				inputStream.close();

				this.fileService.doSetLocaleProperties(parentRepositoryFile.getPath(), RepositoryFile.DEFAULT_LOCALE, localeProperties);
			} catch (Exception ex) {
				FileMetadataImportHandler.LOGGER.error(ex.getMessage());
				throw new PlatformImportException(ex.getMessage());
			}
		}
	}

	private String getParentRepositoryFilePath(RepositoryFileImportBundle importBundle, IRepositoryFileBundle localeFileBundle) {
		String importPath = importBundle.getPath();

		String localeFileName = localeFileBundle.getFile().getName();
		String localeFilePath = localeFileBundle.getPath();
		if (localeFilePath.equals("/") || localeFilePath.equals("\\")) {
			localeFilePath = "";
		}

		String parentFileName = localeFileName.substring(0, localeFileName.length() - FileMetadataImportHandler.LOCALE_EXT.length());
		return RepositoryFilenameUtils.concat(importPath, localeFilePath + parentFileName);
	}

	static class FileMetadataImportSource {
		private ZipInputStream zipInputStream;
		private List<IRepositoryFileBundle> localeFiles;

		private FileMetadataImportSource(final ZipInputStream zipInputStream) {
			this.zipInputStream = zipInputStream;
			this.localeFiles = new ArrayList<>();
			this.initialize();
		}

		private void initialize() {
			try {
				ZipEntry entry = this.zipInputStream.getNextEntry();

				while (entry != null) {
					String entryName = RepositoryFilenameUtils.separatorsToRepository(entry.getName());

					if (entry.isDirectory() || !entryName.endsWith(FileMetadataImportHandler.LOCALE_EXT)) {
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
					this.localeFiles.add(repoFileBundle);

					this.zipInputStream.closeEntry();
					entry = this.zipInputStream.getNextEntry();
				}

				this.zipInputStream.close();
			} catch (IOException ex) {
				FileMetadataImportHandler.LOGGER.error(ex.getMessage());
			}
		}

		private List<IRepositoryFileBundle> getLocaleFiles() {
			return this.localeFiles;
		}
	}

}
