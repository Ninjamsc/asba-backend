package com.technoserv.jms;

/**
 * Created by VBasakov on 20.11.2016.
 */
public class HttpRestClientAlwaysFalse extends HttpRestClient {

    @Override
    public boolean put(String message) {
        return true;
    }
}
