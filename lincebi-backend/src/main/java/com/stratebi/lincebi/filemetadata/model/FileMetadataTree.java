package com.stratebi.lincebi.filemetadata.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class FileMetadataTree {

	@JsonProperty("id")
	private String id;

	@JsonProperty("path")
	private String path;

	@JsonProperty("parent")
	private String parent;

	@JsonProperty("name")
	private String name;

	@JsonProperty("title")
	private String title;

	@JsonProperty("description")
	private String description;

	@JsonProperty("extension")
	private String extension;

	@JsonProperty("openUrl")
	private String openUrl;

	@JsonProperty("editUrl")
	private String editUrl;

	@JsonProperty("created")
	private String created;

	@JsonProperty("modified")
	private String modified;

	@JsonProperty("isFolder")
	private Boolean isFolder;

	@JsonProperty("isHidden")
	private Boolean isHidden;

	@JsonProperty("isHome")
	private Boolean isHome;

	@JsonProperty("isFavorite")
	private Boolean isFavorite;

	@JsonProperty("isRecent")
	private Boolean isRecent;

	@JsonProperty("isReadonly")
	private Boolean isReadonly;

	@JsonProperty("properties")
	private Map<String, String> properties;

	@JsonProperty("children")
	private List<FileMetadataTree> children;

	@JsonProperty("id")
	public String getId() {
		return this.id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public boolean hasId() {
		return this.id != null;
	}

	@JsonProperty("path")
	public String getPath() {
		return this.path;
	}

	@JsonProperty("path")
	public void setPath(String path) {
		this.path = path;
	}

	public boolean hasPath() {
		return this.path != null;
	}

	@JsonProperty("parent")
	public String getParent() {
		return this.parent;
	}

	@JsonProperty("parent")
	public void setParent(String parent) {
		this.parent = parent;
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public boolean hasName() {
		return this.name != null;
	}

	@JsonProperty("title")
	public String getTitle() {
		return this.title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public boolean hasTitle() {
		return this.title != null;
	}

	@JsonProperty("description")
	public String getDescription() {
		return this.description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean hasDescription() {
		return this.description != null;
	}

	@JsonProperty("extension")
	public String getExtension() {
		return this.extension;
	}

	@JsonProperty("extension")
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public boolean hasExtension() {
		return this.extension != null;
	}

	@JsonProperty("openUrl")
	public String getOpenUrl() {
		return this.openUrl;
	}

	@JsonProperty("openUrl")
	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public boolean hasOpenUrl() {
		return this.openUrl != null;
	}

	@JsonProperty("editUrl")
	public String getEditUrl() {
		return this.editUrl;
	}

	@JsonProperty("editUrl")
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}

	public boolean hasEditUrl() {
		return this.editUrl != null;
	}

	@JsonProperty("created")
	public String getCreated() {
		return this.created;
	}

	@JsonProperty("created")
	public void setCreated(String created) {
		this.created = created;
	}

	public boolean hasCreated() {
		return this.created != null;
	}

	@JsonProperty("modified")
	public String getModified() {
		return this.modified;
	}

	@JsonProperty("modified")
	public void setModified(String modified) {
		this.modified = modified;
	}

	public boolean hasModified() {
		return this.modified != null;
	}

	@JsonProperty("isFolder")
	public Boolean getIsFolder() {
		return this.isFolder;
	}

	@JsonProperty("isFolder")
	public void setIsFolder(Boolean isFolder) {
		this.isFolder = isFolder;
	}

	public boolean hasIsFolder() {
		return this.isFolder != null;
	}

	@JsonProperty("isHidden")
	public Boolean getIsHidden() {
		return this.isHidden;
	}

	@JsonProperty("isHidden")
	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean hasIsHidden() {
		return this.isHidden != null;
	}

	@JsonProperty("isHome")
	public Boolean getIsHome() {
		return this.isHome;
	}

	@JsonProperty("isHome")
	public void setIsHome(Boolean isHome) {
		this.isHome = isHome;
	}

	public boolean hasIsHome() {
		return this.isHome != null;
	}

	@JsonProperty("isFavorite")
	public Boolean getIsFavorite() {
		return this.isFavorite;
	}

	@JsonProperty("isFavorite")
	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public boolean hasIsFavorite() {
		return this.isFavorite != null;
	}

	@JsonProperty("isRecent")
	public Boolean getIsRecent() {
		return this.isRecent;
	}

	@JsonProperty("isRecent")
	public void setIsRecent(Boolean isRecent) {
		this.isRecent = isRecent;
	}

	public boolean hasIsRecent() {
		return this.isRecent != null;
	}


	@JsonProperty("isReadonly")
	public Boolean getIsReadonly() {
		return this.isReadonly;
	}

	@JsonProperty("isReadonly")
	public void setIsReadonly(Boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	public boolean hasIsReadonly() {
		return this.isReadonly != null;
	}

	@JsonProperty("properties")
	public Map<String, String> getProperties() {
		return this.properties;
	}

	@JsonProperty("properties")
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public boolean hasProperties() {
		return this.properties != null && this.properties.size() > 0;
	}

	@JsonProperty("children")
	public List<FileMetadataTree> getChildren() {
		return this.children;
	}

	@JsonProperty("children")
	public void setChildren(List<FileMetadataTree> children) {
		this.children = children;
	}

	public boolean hasChildren() {
		return this.children != null && this.children.size() > 0;
	}

}
