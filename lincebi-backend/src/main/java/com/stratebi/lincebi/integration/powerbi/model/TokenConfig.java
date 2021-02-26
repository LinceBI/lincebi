package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenConfig {

	@JsonProperty("token")
	private String token;

	@JsonProperty("tokenId")
	private String tokenId;

	@JsonProperty("expiration")
	private String expiration;

	@JsonProperty("token")
	public String getToken() {
		return this.token;
	}

	@JsonProperty("token")
	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("tokenId")
	public String getTokenId() {
		return this.tokenId;
	}

	@JsonProperty("tokenId")
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@JsonProperty("expiration")
	public String getExpiration() {
		return this.expiration;
	}

	@JsonProperty("expiration")
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

}
