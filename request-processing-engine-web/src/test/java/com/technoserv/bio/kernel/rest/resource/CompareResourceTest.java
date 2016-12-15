package com.technoserv.bio.kernel.rest.resource;

import com.technoserv.bio.kernel.TestUtils;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Adrey on 14.12.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Ignore
public class CompareResourceTest {
    @Autowired
    private CompareResultResource compareResultResource;
    @Autowired
    private CompareResultService compareResultService;
    private static Long WFM = 1L;

    public void setUp() throws Exception {
        String json = TestUtils.readFile("compare-result-view.json");;
        CompareResult compareResult = new CompareResult(WFM, json);
        compareResultService.save(compareResult);
    }

    @Test
    public void findTest() throws Exception {
        setUp();
        String result = (String) compareResultResource.find(WFM).getEntity();
        Assert.assertNotNull(result);
    }

    @Test
    public void find404Test() throws Exception {
        int status = compareResultResource.find(WFM  +1).getStatus();
        Assert.assertEquals(404, status);
    }
}