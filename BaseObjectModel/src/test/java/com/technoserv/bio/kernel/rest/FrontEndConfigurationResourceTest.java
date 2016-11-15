package com.technoserv.bio.kernel.rest;

import com.technoserv.bio.kernel.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.bio.kernel.dao.configuration.impl.FrontEndConfigurationDaoImpl;
import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.service.configuration.api.FrontEndConfigurationService;
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
@ContextConfiguration("/testContext.xml")
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