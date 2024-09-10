package com.stratebi.lincebi.serverinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerInfo {

	@JsonProperty("lincebiVersion")
	private String lincebiVersion;

	@JsonProperty("biserverVersion")
	private String biserverVersion;

	@JsonProperty("instanceId")
	private String instanceId;

	@JsonProperty("lincebiVersion")
	public String getLincebiVersion() {
		return this.lincebiVersion;
	}

	@JsonProperty("lincebiVersion")
	public void setLincebiVersion(String lincebiVersion) {
		this.lincebiVersion = lincebiVersion;
	}

	@JsonProperty("biserverVersion")
	public String getBiserverVersion() {
		return this.biserverVersion;
	}

	@JsonProperty("biserverVersion")
	public void setBiserverVersion(String biserverVersion) {
		this.biserverVersion = biserverVersion;
	}

	@JsonProperty("instanceId")
	public String getInstanceId() {
		return this.instanceId;
	}

	@JsonProperty("instanceId")
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
