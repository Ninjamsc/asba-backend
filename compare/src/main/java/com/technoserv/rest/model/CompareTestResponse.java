package com.technoserv.rest.model;


import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CompareTestResponse {

    private Double[] vector;

    public Double[] getVector() {
        return vector;
    }

    public void setVector(double[] a) {
        vector = new Double[a.length];
        for (int i = 0; i < a.length; i++)
            this.vector[i] = a[i];
    }

}
