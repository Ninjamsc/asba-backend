package com.technoserv.bio.kernel.model.configuration;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Andrey on 15.11.2016.
 */
public class FrontEndConfigurationTest {

    @Test
    public void save() throws Exception {
        int version = 1;
        FrontEndConfiguration frontEndConfiguration = new FrontEndConfiguration();
        frontEndConfiguration.setVersion(version);
        //Ebean.save(frontEndConfiguration);

        FrontEndConfiguration result = null;//Ebean.find(FrontEndConfiguration.class).where().eq("version", version).findList().get(0);

        Assert.assertEquals(frontEndConfiguration.getVersion(), result.getVersion());
    }
}