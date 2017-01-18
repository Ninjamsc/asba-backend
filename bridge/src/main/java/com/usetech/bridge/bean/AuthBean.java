/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;

public class AuthBean implements Serializable {
	private static final long serialVersionUID = -5054749880970511861L;
	@JsonProperty(value = "token")
	@NotNull
	private String token;
	@JsonProperty(value = "username")
	@NotNull
	private String username;

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
		return "AuthBean{token='" + this.token + '\'' +  ", username='" + this.username + '\''  + '}';
	}
}