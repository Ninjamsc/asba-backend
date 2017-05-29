
package com.technoserv.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

public class SkudCompareResponse {


    @JsonInclude(Include.NON_EMPTY)
    private CompareResponsePhotoObject match;

    public CompareResponsePhotoObject getMatch() {
        return match;
    }

    public void setMatch(CompareResponsePhotoObject match) {
        this.match = match;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("match", match)
                .toString();
    }

}
