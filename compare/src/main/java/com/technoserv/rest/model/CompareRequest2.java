package com.technoserv.rest.model;


import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class CompareRequest2 {

@JsonProperty("template_web")	
private String template_web;
@JsonProperty("template_scan")	
private String template_scan;


public String getTemplate_web() {
	return template_web;
}
public void setTemplate_web(String template_web) {
	this.template_web = template_web;
}
public String getTemplate_scan() {
	return template_scan;
}
public void setTemplate_scan(String template_scan) {
	this.template_scan = template_scan;
}

@Override
public String toString() {
	return "CompareRequest  [template_web=" + template_web + ", template_scan="+template_scan+"]";
}
}
