package com.stratebi.lincebi.integration.powerbi.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbedConfig {

	@JsonProperty("embedToken")
	private TokenConfig embedToken;

	@JsonProperty("embedReports")
	private List<ReportConfig> embedReports;

	@JsonProperty("embedToken")
	public TokenConfig getEmbedToken() {
		return this.embedToken;
	}

	@JsonProperty("embedToken")
	public void setEmbedToken(TokenConfig embedToken) {
		this.embedToken = embedToken;
	}

	@JsonProperty("embedReports")
	public List<ReportConfig> getEmbedReports() {
		return this.embedReports;
	}

	@JsonProperty("embedReports")
	public void setEmbedReports(List<ReportConfig> embedReports) {
		this.embedReports = embedReports;
	}

}
