package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Report {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("webUrl")
	private String webUrl;

	@JsonProperty("embedUrl")
	private String embedUrl;

	@JsonProperty("datasetId")
	private String datasetId;

	@JsonProperty("id")
	public String getId() {
		return this.id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
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

	@JsonProperty("datasetId")
	public String getDatasetId() {
		return this.datasetId;
	}

	@JsonProperty("datasetId")
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

}
