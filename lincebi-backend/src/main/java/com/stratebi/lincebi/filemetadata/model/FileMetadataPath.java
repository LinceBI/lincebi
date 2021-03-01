package com.stratebi.lincebi.filemetadata.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetadataPath {

	@JsonProperty("fullPath")
	private String fullPath;

	@JsonProperty("title")
	private String title;

	@JsonProperty("lastUse")
	private Long lastUse;

	@JsonProperty("fullPath")
	public String getFullPath() {
		return this.fullPath;
	}

	@JsonProperty("fullPath")
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public boolean hasFullPath() {
		return this.fullPath != null;
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

	@JsonProperty("lastUse")
	public Long getLastUse() {
		return this.lastUse;
	}

	@JsonProperty("lastUse")
	public void setLastUse(Long lastUse) {
		this.lastUse = lastUse;
	}

	public boolean hasLastUse() {
		return this.lastUse != null;
	}

}
