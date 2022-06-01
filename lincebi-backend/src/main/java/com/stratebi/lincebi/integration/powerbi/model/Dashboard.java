package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dashboard {

	@JsonProperty("id")
	private String id;

	@JsonProperty("displayName")
	private String displayName;

	@JsonProperty("webUrl")
	private String webUrl;

	@JsonProperty("embedUrl")
	private String embedUrl;

	@JsonProperty("id")
	public String getId() {
		return this.id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("displayName")
	public String getDisplayName() {
		return this.displayName;
	}

	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@JsonProperty("webUrl")
	public String getWebUrl() {
		return this.webUrl;
	}

	@JsonProperty("webUrl")
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	@JsonProperty("embedUrl")
	public String getEmbedUrl() {
		return this.embedUrl;
	}

	@JsonProperty("embedUrl")
	public void setEmbedUrl(String embedUrl) {
		this.embedUrl = embedUrl;
	}

}
