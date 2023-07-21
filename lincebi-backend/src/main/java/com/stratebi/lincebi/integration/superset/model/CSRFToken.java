package com.stratebi.lincebi.integration.superset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CSRFToken {

	@JsonProperty("result")
	private String token;

	@JsonProperty("cookie")
	private String cookie;

	@JsonProperty("result")
	public String getToken() {
		return this.token;
	}

	@JsonProperty("result")
	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("cookie")
	public String getCookie() {
		return this.cookie;
	}

	@JsonProperty("cookie")
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}
