package com.stratebi.lincebi.integration.superset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken {

	@JsonProperty("access_token")
	private String token;

	@JsonProperty("access_token")
	public String getToken() {
		return this.token;
	}

	@JsonProperty("access_token")
	public void setToken(String token) {
		this.token = token;
	}

}
