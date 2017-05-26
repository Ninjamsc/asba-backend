package com.technoserv.rest;


import com.technoserv.db.model.configuration.FrontEndConfiguration;
import com.technoserv.db.service.configuration.api.FrontEndConfigurationService;
import com.technoserv.rest.resource.FrontEndConfigurationResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Adrey on 15.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class FrontEndConfigurationResourceTest {

    @Autowired
    private FrontEndConfigurationResource resource;
    @Autowired
    private FrontEndConfigurationService service;

    @Test
    public void get() throws Exception {
        FrontEndConfiguration entity = new FrontEndConfiguration();
        long id = service.save(entity);
        FrontEndConfiguration result = resource.get(id);
        Assert.assertEquals(id, result.getId().longValue());
    }
}