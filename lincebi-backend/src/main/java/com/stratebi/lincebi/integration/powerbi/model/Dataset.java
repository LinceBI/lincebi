package com.stratebi.lincebi.integration.powerbi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Dataset {

	@JsonProperty("id")
	private String id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("isEffectiveIdentityRequired")
	private boolean isEffectiveIdentityRequired;

	@JsonProperty("isEffectiveIdentityRolesRequired")
	private boolean isEffectiveIdentityRolesRequired;

	@JsonProperty("id")
	public String getId() {
		return this.id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("isEffectiveIdentityRequired")
	public boolean getIsEffectiveIdentityRequired() {
		return this.isEffectiveIdentityRequired;
	}

	@JsonProperty("isEffectiveIdentityRequired")
	public void setIsEffectiveIdentityRequired(boolean isEffectiveIdentityRequired) {
		this.isEffectiveIdentityRequired = isEffectiveIdentityRequired;
	}

	@JsonProperty("isEffectiveIdentityRolesRequired")
	public boolean getIsEffectiveIdentityRolesRequired() {
		return this.isEffectiveIdentityRolesRequired;
	}

	@JsonProperty("isEffectiveIdentityRolesRequired")
	public void setIsEffectiveIdentityRolesRequired(boolean isEffectiveIdentityRolesRequired) {
		this.isEffectiveIdentityRolesRequired = isEffectiveIdentityRolesRequired;
	}

}
