package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.rest.request.RequestSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("requestDao")
public class RequestDaoImpl extends AbstractHibernateDao<Long, Request> implements RequestDao {

    private static final Logger log = LoggerFactory.getLogger(RequestDaoImpl.class);

    private static final int AMOUNT_TTL = -1; //Колличество прибавляемых единиц

    private static final int FIELD_TTL = Calendar.MINUTE; //Единицап измерения времени

    public Request findByOrderNumber(Long id) { //TODO ...
        return (Request) getSession().createCriteria(getPersistentClass())
                .add(Property.forName("id").eq(id))
                .uniqueResult();
    }

    /*
     *  Вернуть все заявки, связанные с данным ИИН
     */
    public Collection<Request> findByIin(Long id) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.add(FIELD_TTL, AMOUNT_TTL);
//        Date ttlDate = calendar.getTime();
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Property.forName("person.id").eq(id));
        return criteria.list();
    }

    /**
     * Находим запросы где заполнены все изображения или где прошло более N минут
     *
     * @return
     */
    public List<Request> findNotProcessed() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(FIELD_TTL, AMOUNT_TTL);
        Date ttlDate = calendar.getTime();
        log.debug("findNotProcessed ttlDate: {}", ttlDate);

        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.createCriteria("scannedDocument", "sd");
        criteria.createCriteria("cameraDocument", "cd");

        Conjunction conjunction = Restrictions.conjunction();

        conjunction.add(Property.forName("sd.origImageURL").isNotNull());
        conjunction.add(Property.forName("sd.faceSquare").isNotNull());
        conjunction.add(Property.forName("cd.origImageURL").isNotNull());
        conjunction.add(Property.forName("cd.faceSquare").isNotNull());

        Disjunction disjunction = Restrictions.disjunction();
        disjunction.add(conjunction);

//        disjunction.add(Property.forName("objectDate").le(ttlDate));

        criteria.add(disjunction);

        criteria.add(Property.forName("status").eq(Request.Status.SAVED));
        return criteria.list();
    }

    public List<Request> findByCriteria(RequestSearchCriteria criteria) {
        Criteria c = createSearchCriteria(criteria);
        c.setMaxResults(criteria.getSize());
        c.setFirstResult(criteria.getSize() * criteria.getPage());
        return c.list();
    }

    private Criteria createSearchCriteria(RequestSearchCriteria criteria) {
        Criteria c = getSession().createCriteria(getPersistentClass());
        if (criteria.getFrom() != null) {
            c.add(Property.forName("objectDate").ge(criteria.getFrom()));
        }
        if (criteria.getTo() != null) {
            c.add(Property.forName("objectDate").le(criteria.getTo()));
        }

        if (criteria.getRequestId() != null) {
            c.add(Property.forName("id").eq(criteria.getRequestId()));
        }
        if (criteria.getIin() != null) {
            c.add(Property.forName("person").eq(criteria.getIin()));
        }
        return c;
    }

    public Integer countByCriteria(RequestSearchCriteria criteria) {
        return (Integer) createSearchCriteria(criteria).setProjection(Projections.rowCount()).uniqueResult();
    }
}
