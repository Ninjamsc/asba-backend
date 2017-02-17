package com.technoserv.rest.model;


import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SkudCompareRequest {


	@JsonProperty("template")

	private double[] template;
	@JsonProperty("pictureURL")
	private String pictureURL;

	public String getPictureURL() { return pictureURL; }
	public void setPictureURL(String pictureURL) { this.pictureURL = pictureURL; }
	public double[] getTemplate() { return template; }
	public void setTemplate(double[] template) { this.template = template;}

	@Override
	public String toString() {
		return "SkudCompareRequest  [template_web=" + template + ", pictureURL="+pictureURL+"]";
	}

}