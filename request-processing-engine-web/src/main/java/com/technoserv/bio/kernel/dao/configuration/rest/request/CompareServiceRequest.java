package com.technoserv.bio.kernel.dao.configuration.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CompareServiceRequest {

    @JsonProperty("template_web")
    private double[] webTemplate;
    @JsonProperty("template_scan")
    private double[] scanTemplate;

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
}
