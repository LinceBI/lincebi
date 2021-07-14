package com.stratebi.lincebi.integration.powerbi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbedConfig {

	@JsonProperty("embedToken")
	private EmbedToken embedToken;

	@JsonProperty("reports")
	private List<Report> reports;

	@JsonProperty("datasets")
	private List<Dataset> datasets;
	
	@JsonProperty("embedToken")
	public EmbedToken getEmbedToken() {
		return this.embedToken;
	}

	@JsonProperty("embedToken")
	public void setEmbedToken(EmbedToken embedToken) {
		this.embedToken = embedToken;
	}

	@JsonProperty("reports")
	public List<Report> getReports() {
		return this.reports;
	}

	@JsonProperty("reports")
	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	@JsonProperty("datasets")
	public List<Dataset> getDatasets() {
		return this.datasets;
	}

	@JsonProperty("datasets")
	public void setDatasets(List<Dataset> datasets) {
		this.datasets = datasets;
	}

}
