package com.technoserv.rest.model;


import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CompareTestResponse {

    private Double[] vector;// = {1d,2d,3d,4d};

    public Double[] getVector() {
        return vector;
    }

    public void setVector(double[] a) {
        vector = new Double[a.length];
        for (int i = 0; i < a.length; i++) {
            this.vector[i] = new Double(a[i]);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("vector", vector)
                .toString();
    }
}
