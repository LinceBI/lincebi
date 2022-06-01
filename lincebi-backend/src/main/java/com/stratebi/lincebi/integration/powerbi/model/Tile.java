package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tile {

	@JsonProperty("id")
	private String id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("embedUrl")
	private String embedUrl;

	@JsonProperty("embedData")
	private String embedData;

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

	@JsonProperty("title")
	public String getTitle() {
		return this.title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("embedUrl")
	public String getEmbedUrl() {
		return this.embedUrl;
	}

	@JsonProperty("embedUrl")
	public void setEmbedUrl(String embedUrl) {
		this.embedUrl = embedUrl;
	}

	@JsonProperty("embedData")
	public String getEmbedData() {
		return this.embedData;
	}

	@JsonProperty("embedData")
	public void setEmbedData(String embedData) {
		this.embedData = embedData;
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
