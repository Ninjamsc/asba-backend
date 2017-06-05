package com.technoserv.rest.comparator;

import org.apache.commons.math3.linear.ArrayRealVector;

public class CompareServiceStopListVector {

    private ArrayRealVector vector;

    private Long docId;

    public CompareServiceStopListVector(Long docId, double[] vector) {
        this.setVector(new ArrayRealVector(vector));
        this.setDocId(docId);
    }

    public CompareServiceStopListVector() {

    }

    public ArrayRealVector getVector() {
        return vector;
    }

    public void setVector(ArrayRealVector vector) {
        this.vector = vector;
    }

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }
}
