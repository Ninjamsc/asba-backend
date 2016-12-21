package com.technoserv.rest.model;

/**
 * Created by mlevitin on 21.12.2016.
 */
public class SelfCompareResult
{
    private boolean isSimilar;
    private double threshold;
    private double similarity;

    public boolean isSimilar() {
        return isSimilar;
    }

    public void setSimilar(boolean similar) {
        isSimilar = similar;
    }


    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

}
