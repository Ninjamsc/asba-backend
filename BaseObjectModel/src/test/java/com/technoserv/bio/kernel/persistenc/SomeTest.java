package com.technoserv.bio.kernel.persistenc;

import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.service.configuration.api.FrontEndConfigurationService;
import com.technoserv.bio.kernel.service.configuration.impl.FrontEndConfigurationServiceImpl;
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
public class SomeTest {

    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    private FrontEndConfigurationService frontEndConfigurationService;

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
        List<?> results = currentSession.createQuery("from Foo").list();
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

        assertEquals(1, currentSession.createQuery("from Foo where name = 'Bar'").list().size());
        assertEquals(0, currentSession.createQuery("from Foo where name = 'Baz'").list().size());
    }
}
