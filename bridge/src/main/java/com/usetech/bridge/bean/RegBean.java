/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class RegBean implements Serializable {
	private static final long serialVersionUID = -5054749880970511861L;
	@JsonProperty(value = "UUID")
	@NotNull
	private String uuid;
	@JsonProperty(value = "username")
	@NotNull
	private String username;

	public String getUUID() {
		return this.uuid;
	}

	public void setUUD(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
		return "AuthBean{uuid='" + this.uuid + '\'' +  ", username='" + this.username + '\''  + '}';
	}
}