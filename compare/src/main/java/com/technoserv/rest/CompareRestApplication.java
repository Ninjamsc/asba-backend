package com.technoserv.rest;

import com.technoserv.rest.filters.CORSResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers the components to be used by the JAX-RS application.
 *
 */
public class CompareRestApplication extends ResourceConfig {

    private static final Logger log = LoggerFactory.getLogger(CompareRestApplication.class);

    /**
     * Register JAX-RS application components.
     */
    public CompareRestApplication() {
        log.debug("CompareRestApplication starting...");

        packages("com.technoserv");
        register(CORSResponseFilter.class);
    }
}
