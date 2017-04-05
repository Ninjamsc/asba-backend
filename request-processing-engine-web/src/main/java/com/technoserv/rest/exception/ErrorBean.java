/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.technoserv.rest.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ErrorBean implements Serializable {

	private static final long serialVersionUID = -5054749990970511861L;

	@JsonProperty(value = "message")
	private String message;

	public ErrorBean(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}