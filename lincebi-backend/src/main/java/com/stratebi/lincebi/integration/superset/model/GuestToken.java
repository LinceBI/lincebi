package com.stratebi.lincebi.integration.superset.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuestToken {

	@JsonProperty("token")
	private String token;

	@JsonProperty("expSec")
	private int expSec;

	@JsonProperty("token")
	public String getToken() {
		return this.token;
	}

	@JsonProperty("token")
	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("expSec")
	public int getExpSec() {
		return this.expSec;
	}

	@JsonProperty("expSec")
	public void setExpSec(int expSec) {
		this.expSec = expSec;
	}

}
