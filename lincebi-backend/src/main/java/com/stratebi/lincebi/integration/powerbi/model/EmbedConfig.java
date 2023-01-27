package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class EmbedConfig {

	@JsonProperty("embedToken")
	private EmbedToken embedToken;

	@JsonProperty("workspaceId")
	private String workspaceId;

	@JsonProperty("report")
	private Report report;

	@JsonProperty("dashboard")
	private Dashboard dashboard;

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

	@JsonProperty("workspaceId")
	public String getWorkspaceId() {
		return this.workspaceId;
	}

	@JsonProperty("workspaceId")
	public void setWorkspaceId(String workspaceId) {
		this.workspaceId = workspaceId;
	}

	@JsonProperty("report")
	public Report getReport() {
		return this.report;
	}

	@JsonProperty("report")
	public void setReport(Report report) {
		this.report = report;
	}

	@JsonProperty("dashboard")
	public Dashboard getDashboard() {
		return this.dashboard;
	}

	@JsonProperty("dashboards")
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
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
