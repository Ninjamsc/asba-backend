/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.usetech.bridge.service.LocalDateTimeDeserializer;
import com.usetech.bridge.service.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class FrameBean implements Serializable {
	private static final long serialVersionUID = -5054749880970511861L;
	@JsonProperty(value = "token")
	@NotNull
	private String token;
	@NotNull
	@JsonProperty(value = "wfmId")
	//private String wfmId;
	private Long wfmId;
	@JsonProperty(value = "iin")
	@NotNull
	@Digits(integer = 19, fraction = 0)
	private Long iin;
	@JsonProperty(value = "username")
	@NotNull
	private String username;
	@JsonProperty(value = "timestamp")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@NotNull
	private LocalDateTime timestamp;
	@JsonProperty(value = "type")
	@NotNull
	private String type;
	@JsonProperty(value = "camPic")
	@NotNull
	private String camPic;
	@JsonProperty(value = "scanPic")
	@NotNull
	private String scanPic;

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getWfmId() {
		return this.wfmId;
	}

	public void setWfmId(Long wfmId) {
		this.wfmId = wfmId;
	}

	public Long getIin() {
		return this.iin;
	}

	public void setIin(Long iin) {
		this.iin = iin;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCamPic() {
		return this.camPic;
	}

	public void setCamPic(String camPic) {
		this.camPic = camPic;
	}

	public String getScanPic() {
		return this.scanPic;
	}

	public void setScanPic(String scanPic) {
		this.scanPic = scanPic;
	}

	public String toString() {
		return "FrameBean{token='" + this.token + '\'' + ", wfmId='" + this.wfmId + '\'' + ", iin=" + this.iin
				+ ", username='" + this.username + '\'' + ", timestamp='" + this.timestamp + '\'' + ", type='"
				+ this.type + '\'' + '}';
	}
}