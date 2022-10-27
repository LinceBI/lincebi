package com.stratebi.lincebi.filemetadata.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pentaho.platform.api.engine.IAuthorizationPolicy;
import org.pentaho.platform.api.engine.IContentInfo;
import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.api.engine.IPluginOperation;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.repository2.unified.RepositoryFileTree;
import org.pentaho.platform.api.repository2.unified.RepositoryRequest;
import org.pentaho.platform.api.repository2.unified.RepositoryRequest.FILES_TYPE_FILTER;
import org.pentaho.platform.api.repository2.unified.webservices.StringKeyStringValueDto;
import org.pentaho.platform.api.usersettings.IUserSettingService;
import org.pentaho.platform.api.usersettings.pojo.IUserSetting;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction;
import org.pentaho.platform.security.policy.rolebased.actions.RepositoryCreateAction;
import org.pentaho.platform.security.policy.rolebased.actions.RepositoryReadAction;
import org.pentaho.platform.util.RepositoryPathEncoder;
import org.pentaho.platform.web.http.api.resources.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratebi.lincebi.filemetadata.exception.FileMetadataAdministerException;
import com.stratebi.lincebi.filemetadata.exception.FileMetadataReadException;
import com.stratebi.lincebi.filemetadata.exception.FileMetadataWriteException;
import com.stratebi.lincebi.filemetadata.model.FileMetadataPath;
import com.stratebi.lincebi.filemetadata.model.FileMetadataTree;

public class FileMetadataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileMetadataService.class);

	private static final TimeZone TZ = TimeZone.getTimeZone("UTC");
	private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	static {
		FileMetadataService.DATEFORMAT.setTimeZone(FileMetadataService.TZ);
	}

	private static final String SHOW_HIDDEN_FILES_USER_SETTING = "MANTLE_SHOW_HIDDEN_FILES";
	private static final String HOME_USER_SETTING = "home";
	private static final String FAVORITES_USER_SETTING = "favorites";
	private static final String RECENTS_USER_SETTING = "recent";

	private static final String RUN_OPERATION_ID = "RUN";
	private static final String EDIT_OPERATION_ID = "EDIT";

	// The content of these paths will be ignored
	private static final List<String> FORBIDDEN_PATHS = Arrays.asList(
		"/etc",
		"/public/plugin-samples"
	);
	// Transform every forbidden path into a regex
	private static final List<Pattern> FORBIDDEN_PATHS_PATTERNS = new ArrayList<>();
	static {
		for (String path : FileMetadataService.FORBIDDEN_PATHS) {
			Pattern pattern = Pattern.compile("^" + Pattern.quote(path) + "(?:/.*)?$");
			FileMetadataService.FORBIDDEN_PATHS_PATTERNS.add(pattern);
		}
	}

	// These properties are locale-independent
	private static final Set<String> GENERIC_LOCALE_PROPERTIES = new HashSet<>(Arrays.asList(
		"embedded",
		"tags",
		"thumbnail"
	));

	private String userName;
	private String fullyQualifiedServerUrl;
	private IPluginManager pluginManager;
	private IUserSettingService userSettingService;
	private IAuthorizationPolicy authorizationPolicy;
	private FileService fileService;

	private boolean canRead;
	private boolean canWrite;
	private boolean canAdminister;

	private Map<String, Map<String, String>> extensionPerspectivesMap;

	private boolean showHiddenFiles;
	private Set<RepositoryFile> home;
	private Set<RepositoryFile> favorites;
	private Set<RepositoryFile> recents;

	public FileMetadataService() {
		this.userName = PentahoSessionHolder.getSession().getName();
		this.fullyQualifiedServerUrl = PentahoSystem.getApplicationContext().getFullyQualifiedServerURL();
		this.pluginManager = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
		this.userSettingService = PentahoSystem.get(IUserSettingService.class, PentahoSessionHolder.getSession());
		this.authorizationPolicy = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession());
		this.fileService = new FileService();

		this.canRead = this.authorizationPolicy.isAllowed(RepositoryReadAction.NAME);
		this.canWrite = this.authorizationPolicy.isAllowed(RepositoryCreateAction.NAME);
		this.canAdminister = this.authorizationPolicy.isAllowed(AdministerSecurityAction.NAME);

		this.extensionPerspectivesMap = this.getExtensionPerspectivesMap();

		this.showHiddenFiles = this.getBooleanUserSetting(FileMetadataService.SHOW_HIDDEN_FILES_USER_SETTING, false);
		this.home = this.getFileListUserSetting(FileMetadataService.HOME_USER_SETTING, true);
		this.favorites = this.getFileListUserSetting(FileMetadataService.FAVORITES_USER_SETTING, false);
		this.recents = this.getFileListUserSetting(FileMetadataService.RECENTS_USER_SETTING, false);
	}

	public FileMetadataTree getFileMetadata(FileMetadataPath fileMetadataPath, String locale, String showHidden, int depth) {
		RepositoryFile repositoryFile = this.fileService.getRepository().getFile(fileMetadataPath.getFullPath());
		if (repositoryFile == null) return null;

		boolean showHiddenBool = showHidden.equals("auto") ? this.showHiddenFiles : showHidden.equals("true");

		RepositoryRequest repositoryRequest = new RepositoryRequest(repositoryFile.getPath(), showHiddenBool, depth, null);
		repositoryRequest.setTypes(FILES_TYPE_FILTER.FILES_FOLDERS);
		repositoryRequest.setIncludeAcls(false);
		repositoryRequest.setIncludeSystemFolders(false);

		RepositoryFileTree repositoryFileTree = fileService.getRepository().getTree(repositoryRequest);

		return this.getFileMetadata(repositoryFileTree, locale);
	}

	private FileMetadataTree getFileMetadata(RepositoryFileTree repositoryFileTree, String locale) {
		getFileMetadata: try {
			RepositoryFile repositoryFile = repositoryFileTree.getFile();

			String path = repositoryFile.getPath();

			/* =====================
			 * = Check permissions =
			 * =====================
			 */

			if (this.isInForbiddenPaths(path)) {
				break getFileMetadata;
			}

			if (!this.canRead) {
				throw new FileMetadataReadException(this.userName);
			} else if (!this.canReadFile(path)) {
				throw new FileMetadataReadException(this.userName, path);
			}

			/* ================
			 * = Get metadata =
			 * ================
			 */

			FileMetadataTree fileMetadataTree = new FileMetadataTree();

			String id = repositoryFile.getId().toString();
			fileMetadataTree.setId(id);

			//String path = repositoryFile.getPath();
			fileMetadataTree.setPath(path);

			String name = repositoryFile.getName();
			fileMetadataTree.setName(name);

			String parent;
			if (!path.equals("/")) {
				parent = path.substring(0, path.length() - name.length());
			} else {
				parent = null;
			}
			fileMetadataTree.setParent(parent);

			boolean isFolder = repositoryFile.isFolder();
			fileMetadataTree.setIsFolder(isFolder);

			boolean isHidden = repositoryFile.isHidden();
			fileMetadataTree.setIsHidden(isHidden);

			if (!isFolder) {
				Map<String, String> fileMetadataTreeProperties = new HashMap<>();
				Map<String, Properties> localePropertiesMap = repositoryFile.getLocalePropertiesMap();
				if (localePropertiesMap != null) {
					// Default locale properties
					Properties defaultLocaleProperties = localePropertiesMap.get(RepositoryFile.DEFAULT_LOCALE);
					if (defaultLocaleProperties != null) {
						defaultLocaleProperties.forEach((key, value) -> {
							String localePropertyKey = key.toString();
							String localePropertyValue = value.toString();
							fileMetadataTreeProperties.put(localePropertyKey, localePropertyValue);
						});
					}
					// Current locale properties
					if (!locale.equalsIgnoreCase(RepositoryFile.DEFAULT_LOCALE)) {
						Properties localeProperties = localePropertiesMap.get(locale);
						if (localeProperties != null) {
							localeProperties.forEach((key, value) -> {
								String localePropertyKey = key.toString();
								// Skip if the property is generic
								if (!FileMetadataService.GENERIC_LOCALE_PROPERTIES.contains(localePropertyKey)) {
									String localePropertyValue = value.toString();
									fileMetadataTreeProperties.put(localePropertyKey, localePropertyValue);
								}
							});
						}
					}
				}
				fileMetadataTree.setProperties(fileMetadataTreeProperties);

				String title;
				if (fileMetadataTreeProperties.containsKey(RepositoryFile.FILE_TITLE)) {
					title = fileMetadataTreeProperties.get(RepositoryFile.FILE_TITLE);
				} else {
					title = name;
				}
				fileMetadataTree.setTitle(title);

				String description;
				if (fileMetadataTreeProperties.containsKey(RepositoryFile.FILE_DESCRIPTION)) {
					description = fileMetadataTreeProperties.get(RepositoryFile.FILE_DESCRIPTION);
				} else {
					description = "";
				}
				fileMetadataTree.setDescription(description);

				int indexOfExtension = name.lastIndexOf('.');
				String extension;
				if (indexOfExtension != -1) {
					extension = name.substring(indexOfExtension + 1);
				} else {
					extension = "";
				}
				fileMetadataTree.setExtension(extension);

				Date createdDate = repositoryFile.getCreatedDate();
				String created = FileMetadataService.DATEFORMAT.format(createdDate);
				fileMetadataTree.setCreated(created);

				Date modifiedDate = repositoryFile.getLastModifiedDate();
				String modified;
				if (modifiedDate != null) {
					modified = FileMetadataService.DATEFORMAT.format(modifiedDate);
				} else {
					modified = created;
				}
				fileMetadataTree.setModified(modified);

				Map<String, String> perspectivesMap = this.extensionPerspectivesMap.get(extension);

				String openUrl;
				if (perspectivesMap != null && perspectivesMap.containsKey(FileMetadataService.RUN_OPERATION_ID)) {
					openUrl = this.getBaseUrlFromPath(path) + perspectivesMap.get(FileMetadataService.RUN_OPERATION_ID);
				} else {
					openUrl = this.getBaseUrlFromPath(path) + "content";
				}
				fileMetadataTree.setOpenUrl(openUrl);

				String editUrl;
				if (perspectivesMap != null && perspectivesMap.containsKey(FileMetadataService.EDIT_OPERATION_ID)) {
					editUrl = this.getBaseUrlFromPath(path) + perspectivesMap.get(FileMetadataService.EDIT_OPERATION_ID);
				} else {
					editUrl = null;
				}
				fileMetadataTree.setEditUrl(editUrl);

				boolean isHome = this.home.contains(repositoryFile);
				fileMetadataTree.setIsHome(isHome);

				boolean isFavorite = this.favorites.contains(repositoryFile);
				fileMetadataTree.setIsFavorite(isFavorite);

				boolean isRecent = this.recents.contains(repositoryFile);
				fileMetadataTree.setIsRecent(isRecent);

				boolean isReadonly = !this.canWriteFile(path);
				fileMetadataTree.setIsReadonly(isReadonly);
			} else {
				List<FileMetadataTree> fileMetadataChildren = new ArrayList<>();
				List<RepositoryFileTree> repositoryFileTreeChildren = repositoryFileTree.getChildren();
				for (RepositoryFileTree repositoryFileTreeChild : repositoryFileTreeChildren) {
					FileMetadataTree fileMetadataChild = this.getFileMetadata(repositoryFileTreeChild, locale);
					if (fileMetadataChild != null) {
						fileMetadataChildren.add(fileMetadataChild);
					}
				}
				fileMetadataTree.setChildren(fileMetadataChildren);
			}

			return fileMetadataTree;
		} catch (Exception ex) {
			FileMetadataService.LOGGER.error(ex.getMessage());
		}

		return null;
	}

	public FileMetadataPath setFileMetadata(FileMetadataTree fileMetadataTree, String locale) {
		setFileMetadata: try {
			RepositoryFile repositoryFile = this.fileService.getRepository().getFile(fileMetadataTree.getPath());
			if (repositoryFile == null) return null;

			String path = repositoryFile.getPath();

			/* =====================
			 * = Check permissions =
			 * =====================
			 */

			if (this.isInForbiddenPaths(path)) {
				break setFileMetadata;
			}

			boolean isFolder = repositoryFile.isFolder();
			if (isFolder) {
				throw new FileMetadataWriteException(this.userName, path);
			}

			if (fileMetadataTree.hasTitle()) {
				if (!this.canWrite) {
					throw new FileMetadataWriteException(this.userName);
				} else if (!this.canWriteFile(path)) {
					throw new FileMetadataWriteException(this.userName, path, "title");
				}
			}

			if (fileMetadataTree.hasDescription()) {
				if (!this.canWrite) {
					throw new FileMetadataWriteException(this.userName);
				} else if (!this.canWriteFile(path)) {
					throw new FileMetadataWriteException(this.userName, path, "description");
				}
			}

			if (fileMetadataTree.hasProperties()) {
				if (!this.canWrite) {
					throw new FileMetadataWriteException(this.userName);
				} else if (!this.canWriteFile(path)) {
					throw new FileMetadataWriteException(this.userName, path, "properties");
				}
			}

			if (fileMetadataTree.hasIsHome() && !this.canAdminister) {
				throw new FileMetadataAdministerException(this.userName);
			}

			/* ================
			 * = Set metadata =
			 * ================
			 */

			Map<String, String> fileMetadataTreeProperties = fileMetadataTree.hasProperties()
				? fileMetadataTree.getProperties()
				: new HashMap<>();

			if (fileMetadataTree.hasTitle()) {
				String title = fileMetadataTree.getTitle();
				fileMetadataTreeProperties.put(RepositoryFile.FILE_TITLE, title);
			}

			if (fileMetadataTree.hasDescription()) {
				String description = fileMetadataTree.getDescription();
				fileMetadataTreeProperties.put(RepositoryFile.FILE_DESCRIPTION, description);
			}

			if (fileMetadataTree.hasIsHome()) {
				Set<RepositoryFile> fileSet = new HashSet<>(Collections.singletonList(repositoryFile));
				boolean isHome = fileMetadataTree.getIsHome();
				this.toggleFileListUserSetting(FileMetadataService.HOME_USER_SETTING, fileSet, isHome, true);
			}

			if (fileMetadataTree.hasIsFavorite()) {
				Set<RepositoryFile> fileSet = new HashSet<>(Collections.singletonList(repositoryFile));
				boolean isFavorite = fileMetadataTree.getIsFavorite();
				this.toggleFileListUserSetting(FileMetadataService.FAVORITES_USER_SETTING, fileSet, isFavorite, false);
			}

			if (fileMetadataTree.hasIsRecent()) {
				Set<RepositoryFile> fileSet = new HashSet<>(Collections.singletonList(repositoryFile));
				boolean isRecent = fileMetadataTree.getIsRecent();
				this.toggleFileListUserSetting(FileMetadataService.RECENTS_USER_SETTING, fileSet, isRecent, false);
			}


			if (fileMetadataTreeProperties.size() > 0) {
				List<StringKeyStringValueDto> defaultLocaleProperties = new ArrayList<>();
				List<StringKeyStringValueDto> localeProperties = new ArrayList<>();
				for (Map.Entry<String, String> localeProperty : fileMetadataTreeProperties.entrySet()) {
					String localePropertyKey = localeProperty.getKey();
					String localePropertyValue = localeProperty.getValue();
					StringKeyStringValueDto keyStringValue = new StringKeyStringValueDto(localePropertyKey, localePropertyValue);
					if (FileMetadataService.GENERIC_LOCALE_PROPERTIES.contains(localePropertyKey)) {
						defaultLocaleProperties.add(keyStringValue);
					} else {
						localeProperties.add(keyStringValue);
					}
				}
				if (!defaultLocaleProperties.isEmpty()) {
					fileService.doSetLocaleProperties(repositoryFile.getPath(), RepositoryFile.DEFAULT_LOCALE, defaultLocaleProperties);
				}
				if (!localeProperties.isEmpty()) {
					fileService.doSetLocaleProperties(repositoryFile.getPath(), locale, localeProperties);
				}
			}

			FileMetadataPath fileMetadataPath = new FileMetadataPath();
			fileMetadataPath.setFullPath(path);

			return fileMetadataPath;
		} catch (Exception ex) {
			FileMetadataService.LOGGER.error(ex.getMessage());
		}

		return null;
	}

	private boolean isInForbiddenPaths(String path) {
		for (Pattern pattern : FileMetadataService.FORBIDDEN_PATHS_PATTERNS) {
			if (pattern.matcher(path).matches()) return true;
		}

		return false;
	}

	private boolean canAccessFile(String path, String permissions) {
		return this.fileService.doGetCanAccess(path, permissions).equals("true");
	}

	private boolean canReadFile(String path) {
		return this.canAccessFile(path, "0");
	}

	private boolean canWriteFile(String path) {
		return this.canAccessFile(path, "1");
	}

	private Map<String, Map<String, String>> getExtensionPerspectivesMap() {
		Map<String, Map<String, String>> extensionPerspectivesMap = new HashMap<>();

		for (String contentType : this.pluginManager.getContentTypes()) {
			Map<String, String> perspectivesMap = new HashMap<>();

			IContentInfo contentInfo = this.pluginManager.getContentTypeInfo(contentType);
			String fileExtension = contentInfo.getExtension();

			String runPerspective = "generatedContent";
			String editPerspective = null;

			List<IPluginOperation> fileOperations = contentInfo.getOperations();
			for (IPluginOperation operation : fileOperations) {
				String operationId = operation.getId();
				String operationPerspective = operation.getPerspective();
				if (operationPerspective != null) {
					switch (operationId) {
						case FileMetadataService.RUN_OPERATION_ID:
							runPerspective = operationPerspective;
							break;
						case FileMetadataService.EDIT_OPERATION_ID:
							editPerspective = operationPerspective;
							break;
						}
				}
			}

			if (runPerspective != null) {
				perspectivesMap.put(FileMetadataService.RUN_OPERATION_ID, runPerspective);
			}
			if (editPerspective != null) {
				perspectivesMap.put(FileMetadataService.EDIT_OPERATION_ID, editPerspective);
			}

			extensionPerspectivesMap.put(fileExtension, perspectivesMap);
		}

		return extensionPerspectivesMap;
	}

	private String getBaseUrlFromPath(String path) {
		return this.fullyQualifiedServerUrl + "api/repos/" + RepositoryPathEncoder.encodeRepositoryPath(path) + "/";
	}

	private IUserSetting getUserSetting(String settingName, String defaultValue, boolean isGlobal) {
		if (isGlobal) {
			return this.userSettingService.getGlobalUserSetting(settingName, defaultValue);
		} else {
			return this.userSettingService.getUserSetting(settingName, defaultValue);
		}
	}

	private void setUserSetting(String settingName, String settingValue, boolean isGlobal) {
		if (isGlobal) {
			this.userSettingService.setGlobalUserSetting(settingName, settingValue);
		} else {
			this.userSettingService.setUserSetting(settingName, settingValue);
		}
	}

	private boolean getBooleanUserSetting(String settingName, boolean isGlobal) {
		return this.getUserSetting(settingName, "false", isGlobal).getSettingValue().equals("true");
	}

	private Set<RepositoryFile> getFileListUserSetting(String settingName, boolean isGlobal) {
		Set<RepositoryFile> repositoryFileSet = new HashSet<>();

		ObjectMapper mapper = new ObjectMapper();

		List<FileMetadataPath> fileMetadataPathList;
		try {
			String settingValue = this.getUserSetting(settingName, "[]", isGlobal).getSettingValue();
			fileMetadataPathList = Arrays.asList(mapper.readValue(settingValue, FileMetadataPath[].class));
		} catch (JsonProcessingException ex) {
			FileMetadataService.LOGGER.error(ex.getMessage());
			fileMetadataPathList = null;
		}

		if (fileMetadataPathList != null) {
			for (FileMetadataPath fileMetadataPath : fileMetadataPathList) {
				String path = fileMetadataPath.getFullPath();
				RepositoryFile repositoryFile = this.fileService.getRepository().getFile(path);
				if (repositoryFile != null) {
					repositoryFileSet.add(repositoryFile);
				}
			}
		}

		return repositoryFileSet;
	}

	private void setFileListUserSetting(String settingName, Set<RepositoryFile> repositoryFileSet, boolean isGlobal) {
		String settingValue;

		ObjectMapper mapper = new ObjectMapper();

		List<FileMetadataPath> fileMetadataPathList = new ArrayList<>();
		for (RepositoryFile repositoryFile : repositoryFileSet) {
			FileMetadataPath fileMetadataPath = new FileMetadataPath();
			fileMetadataPath.setFullPath(repositoryFile.getPath());
			fileMetadataPath.setTitle(repositoryFile.getTitle());
			fileMetadataPath.setLastUse(repositoryFile.getLastModifiedDate().getTime());
			fileMetadataPathList.add(fileMetadataPath);
		}

		try {
			settingValue = mapper.writeValueAsString(fileMetadataPathList);
		} catch (JsonProcessingException ex) {
			FileMetadataService.LOGGER.error(ex.getMessage());
			settingValue = null;
		}

		if (settingValue != null) {
			this.setUserSetting(settingName, settingValue, isGlobal);
		}
	}

	private void toggleFileListUserSetting(String settingName, Set<RepositoryFile> toggleFiles, boolean isAdd, boolean isGlobal) {
		Set<RepositoryFile> currentFiles = this.getFileListUserSetting(settingName, isGlobal);

		if (isAdd) currentFiles.addAll(toggleFiles);
		else currentFiles.removeAll(toggleFiles);

		this.setFileListUserSetting(settingName, currentFiles, isGlobal);
	}

}
