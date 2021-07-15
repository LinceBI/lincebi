package com.stratebi.lincebi.integration.powerbi.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbedConfig {

	@JsonProperty("embedToken")
	private EmbedToken embedToken;

	@JsonProperty("reports")
	private Set<Report> reports;

	@JsonProperty("datasets")
	private Set<Dataset> datasets;
	
	@JsonProperty("embedToken")
	public EmbedToken getEmbedToken() {
		return this.embedToken;
	}

	@JsonProperty("embedToken")
	public void setEmbedToken(EmbedToken embedToken) {
		this.embedToken = embedToken;
	}

	@JsonProperty("reports")
	public Set<Report> getReports() {
		return this.reports;
	}

	@JsonProperty("reports")
	public void setReports(Set<Report> reports) {
		this.reports = reports;
	}

	@JsonProperty("datasets")
	public Set<Dataset> getDatasets() {
		return this.datasets;
	}

	@JsonProperty("datasets")
	public void setDatasets(Set<Dataset> datasets) {
		this.datasets = datasets;
	}

}
