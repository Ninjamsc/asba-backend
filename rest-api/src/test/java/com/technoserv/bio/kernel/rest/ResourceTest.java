package com.technoserv.bio.kernel.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Application;

/**
 * Created by Andrey on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
public class ResourceTest extends JerseyTest {

//    @Override
//    protected Application configure() {
//        ResourceConfig rc = new ResourceConfig();
//
//        rc.register(SpringLifecycleListener.class);
//        rc.register(FrontEndConfigurationResource.class);
//        rc.register(RequestContextFilter.class);
//
//        rc.property("contextConfigLocation", "classpath:testContext.xml");
//        return rc;
//    }

    @Override
    public Application configure() {
        return new ResourceConfig().packages("com.technoserv.bio.kernel");
    }

    @Test
    @Ignore
    public void test() { //tech-1.0.0/api/v1/front-end-configuration
        final String hello = target("/api/v1/front-end-configuration").request().get(String.class);
        Assert.assertEquals("Hello World!", hello);
    }
}