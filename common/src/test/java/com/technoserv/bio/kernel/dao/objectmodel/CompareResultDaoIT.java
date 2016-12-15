package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.CompareResultDao;
import com.technoserv.db.model.objectmodel.CompareResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContextExtDS.xml")
@Transactional
public class CompareResultDaoIT {

    @Autowired
    private CompareResultDao dao;

    @Test
    public void saveEncoded() throws Exception {
        final Long id = 1L;
        CompareResult compareResult = new CompareResult(id, "['ВАСЯ', 'ЛЮДА']");
        dao.save(compareResult);
        System.out.println(dao.get(id).getJson());
    }
}