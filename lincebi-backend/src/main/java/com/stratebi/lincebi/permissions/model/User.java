package com.stratebi.lincebi.permissions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	@JsonProperty("name")
	private String name;

	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

}
