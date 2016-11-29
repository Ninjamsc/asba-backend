package com.technoserv.bio.kernel.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompareServiceRequest {

    @JsonProperty("template_web")
    private double[] webTemplate;
    @JsonProperty("template_scan")
    private double[] scanTemplate;

    @JsonProperty("scanFullFrameURL")
    private String scanFullFrameURL;
    @JsonProperty("scanPreviewURL")
    private String scanPreviewURL;

    @JsonProperty("webFullFrameURL")
    private String webFullFrameURL;
    @JsonProperty("webPreviewURL")
    private String webPreviewURL;

    public double[] getWebTemplate() {
        return webTemplate;
    }
    public void setWebTemplate(double[] webTemplate) {
        this.webTemplate = webTemplate;
    }

    public double[] getScanTemplate() {
        return scanTemplate;
    }
    public void setScanTemplate(double[] scanTemplate) {
        this.scanTemplate = scanTemplate;
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
    public String getScanFullFrameURL() {
        return scanFullFrameURL;
    }
    public void setScanFullFrameURL(String scanFullFrameURL) {
        this.scanFullFrameURL = scanFullFrameURL;
    }



}
