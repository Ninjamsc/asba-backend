package com.technoserv.rest.model;

import com.google.common.base.MoreObjects;

public class CompareResponsePhotoObject {

    private String url;
    private double similarity;
    private Long identity;


    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompareResponsePhotoObject that = (CompareResponsePhotoObject) o;

        if (Double.compare(that.similarity, similarity) != 0) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return identity != null ? identity.equals(that.identity) : that.identity == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = url != null ? url.hashCode() : 0;
        temp = Double.doubleToLongBits(similarity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (identity != null ? identity.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("similarity", similarity)
                .add("identity", identity)
                .toString();
    }
}
