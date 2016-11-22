package com.technoserv.bio.kernel.rest.request;

import java.util.Collection;

public class SomeRequest {

    private Collection<String> ids;

    public Collection<String> getIds() {
        return ids;
    }
    public void setIds(Collection<String> ids) {
        this.ids = ids;
    }
}
