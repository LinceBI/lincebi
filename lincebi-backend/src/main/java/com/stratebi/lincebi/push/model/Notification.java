package com.stratebi.lincebi.push.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

	@JsonProperty("title")
	private String title;

	@JsonProperty("body")
	private String body;

	@JsonProperty("title")
	public String getTitle() {
		return this.title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("body")
	public String getBody() {
		return this.body;
	}

	@JsonProperty("body")
	public void setBody(String body) {
		this.body = body;
	}

}
