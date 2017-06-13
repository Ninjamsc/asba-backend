package com.technoserv.rest.response;

import com.google.common.base.MoreObjects;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class PhotoAnalyzeResult {

    public Integer problem;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("problem", problem)
                .toString();
    }
}
