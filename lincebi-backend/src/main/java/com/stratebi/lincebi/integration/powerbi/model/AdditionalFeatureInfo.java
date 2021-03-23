package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalFeatureInfo {

	@JsonProperty("usage")
	private Float usage;

	@JsonProperty("usage")
	public Float getUsage() {
		return this.usage;
	}

	@JsonProperty("usage")
	public void setUsage(Float usage) {
		this.usage = usage;
	}

}
