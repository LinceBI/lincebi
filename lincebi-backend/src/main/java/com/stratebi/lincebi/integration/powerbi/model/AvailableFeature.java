package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailableFeature {

	@JsonProperty("name")
	private String name;

	@JsonProperty("state")
	private String state;

	@JsonProperty("extendedState")
	private String extendedState;

	@JsonProperty("additionalInfo")
	private AdditionalFeatureInfo additionalInfo;

	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("state")
	public String getState() {
		return this.state;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("extendedState")
	public String getExtendedState() {
		return this.extendedState;
	}

	@JsonProperty("extendedState")
	public void setExtendedState(String extendedState) {
		this.extendedState = extendedState;
	}

	@JsonProperty("additionalInfo")
	public AdditionalFeatureInfo getAdditionalInfo() {
		return this.additionalInfo;
	}

	@JsonProperty("additionalInfo")
	public void setAdditionalInfo(AdditionalFeatureInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}
