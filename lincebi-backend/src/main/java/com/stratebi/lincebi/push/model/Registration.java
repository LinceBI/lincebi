package com.stratebi.lincebi.push.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Registration {

	@JsonProperty("device")
	private String device;

	@JsonProperty("instance")
	private String instance;

	@JsonProperty("secret")
	private String secret;

	@JsonProperty("device")
	public String getDevice() {
		return this.device;
	}

	@JsonProperty("device")
	public void setDevice(String device) {
		this.device = device;
	}

	@JsonProperty("instance")
	public String getInstance() {
		return this.instance;
	}

	@JsonProperty("instance")
	public void setInstance(String instance) {
		this.instance = instance;
	}

	@JsonProperty("secret")
	public String getSecret() {
		return this.secret;
	}

	@JsonProperty("secret")
	public void setSecret(String secret) {
		this.secret = secret;
	}

}
