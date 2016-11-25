package com.technoserv.bio.kernel.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompareServiceRequest {

    @JsonProperty("template_web")
    private String webTemplate;
    @JsonProperty("template_scan")
    private String scanTemplate;

    public String getWebTemplate() {
        return webTemplate;
    }
    public void setWebTemplate(String webTemplate) {
        this.webTemplate = webTemplate;
    }

    public String getScanTemplate() {
        return scanTemplate;
    }
    public void setScanTemplate(String scanTemplate) {
        this.scanTemplate = scanTemplate;
    }
}
