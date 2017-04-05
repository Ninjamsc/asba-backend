package com.technoserv.rest.model;


		import com.fasterxml.jackson.annotation.JsonProperty;

public class CompareRequest {

	@JsonProperty("template_web")
	private double[] template_web;
	@JsonProperty("template_scan")
	private double[] template_scan;
	@JsonProperty("scanFullFrameURL")
	private String scanFullFrameURL;
	@JsonProperty("scanPreviewURL")
	private String scanPreviewURL;
	@JsonProperty("webFullFrameURL")
	private String webFullFrameURL;
	@JsonProperty("webPreviewURL")
	private String webPreviewURL;
	@JsonProperty("iin")
	private Long iin;
	@JsonProperty("wfmId")
	private Long wfmId;

	public Long getWfmId() {return wfmId;}
	public void setWfmId(Long wfmId) {this.wfmId = wfmId;}
	public Long getIin() {
		return iin;
	}
	public void setIin(Long iin) {
		this.iin = iin;
	}
	public double[] getTemplate_web() {
		return template_web;
	}
	public void setTemplate_web(double[] template_web) {
		this.template_web = template_web;
	}
	public double[] getTemplate_scan() {return template_scan;}
	public void setTemplate_scan(double[] template_scan) {
		this.template_scan = template_scan;
	}
	public String getScanFullFrameURL() {
		return scanFullFrameURL;
	}
	public void setScanFullFrameURL(String scanFullFrameURL) {
		this.scanFullFrameURL = scanFullFrameURL;
	}
	public String getScanPreviewURL() {
		return scanPreviewURL;
	}
	public void setScanPreviewURL(String scanPreviewURL) {
		this.scanPreviewURL = scanPreviewURL;
	}
	public String getWebFullFrameURL() {
		return webFullFrameURL;
	}
	public void setWebFullFrameURL(String webFullFrameURL) {
		this.webFullFrameURL = webFullFrameURL;
	}
	public String getWebPreviewURL() {
		return webPreviewURL;
	}
	public void setWebPreviewURL(String webPreviewURL) {
		this.webPreviewURL = webPreviewURL;
	}

	@Override
	public String toString() {
		return "CompareRequest  [template_web=" + template_web + ", template_scan="+template_scan+"]";
	}

}
