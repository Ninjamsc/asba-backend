package com.technoserv.rest.response;

import com.google.common.base.MoreObjects;

public class SomeResponse {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", text)
                .toString();
    }
}
