package com.technoserv.bio.kernel.persistence;

import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by VBasakov on 15.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class FrontEndConfigurationTest {

    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession;


    @Before
    public void openSession() {
        currentSession = sessionFactory.getCurrentSession();
    }

    @Test
    public void shouldHaveASessionFactory() {
        assertNotNull(sessionFactory);
    }

    @Test
    public void shouldHaveNoObjectsAtStart() {
        List<?> results = currentSession.createQuery("from FrontEndConfiguration").list();
        assertTrue(results.isEmpty());
    }

    @Test
    public void shouldBeAbleToPersistAnObject() {
        assertEquals(0, currentSession.createQuery("from FrontEndConfiguration").list().size());
        FrontEndConfiguration feConf = new FrontEndConfiguration();
        feConf.setVersion(1);
        currentSession.persist(feConf);
        currentSession.flush();
        assertEquals(1, currentSession.createQuery("from FrontEndConfiguration").list().size());
    }

    @Test
    public void shouldBeABleToQueryForObjects() {
        shouldBeAbleToPersistAnObject();

        assertEquals(1, currentSession.createQuery("from FrontEndConfiguration where version = 1").list().size());
        assertEquals(0, currentSession.createQuery("from FrontEndConfiguration where version = 2").list().size());
    }
}
