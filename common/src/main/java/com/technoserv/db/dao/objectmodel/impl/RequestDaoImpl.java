package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Request;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("requestDao")
public class RequestDaoImpl extends AbstractHibernateDao<Long, Request> implements RequestDao {

    private static final int AMOUNT_TTL = -1;//Колличество прибавляемых единиц
    private static final int FIELD_TTL = Calendar.MINUTE;//Единицап измерения времени

    public Request findByOrderNumber(Long id) { //TODO ...
        return (Request) getSession().createCriteria(getPersistentClass())
                .add(Property.forName("id").eq(id)).uniqueResult();
    }

    /*
     *  Вернуть все заявки, связанные с данным ИИН
     */
    public Collection<Request> findByIin(Long id)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(FIELD_TTL, AMOUNT_TTL);
        Date ttlDate = calendar.getTime();
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Property.forName("person_id").eq(id));
        return criteria.list();

    }
    /**
     * Находим запросы где заполнены все изображения или где прошло более N минут
     * @return
     */
    public Collection<Request> findNotProcessed() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(FIELD_TTL, AMOUNT_TTL);
        Date ttlDate = calendar.getTime();
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.createCriteria("scannedDocument","sd");
        criteria.createCriteria("cameraDocument","cd");

        Disjunction disjunction = Restrictions.disjunction();

        Conjunction conjunction = Restrictions.conjunction();
        conjunction.add(Property.forName("sd.origImageURL").isNotNull());
        conjunction.add(Property.forName("sd.faceSquare").isNotNull());
        conjunction.add(Property.forName("cd.origImageURL").isNotNull());
        conjunction.add(Property.forName("cd.faceSquare").isNotNull());
        disjunction.add(conjunction);
        disjunction.add(Property.forName("objectDate").le(ttlDate));
        criteria.add(disjunction);

        criteria.add(Property.forName("status").eq(Request.Status.SAVED));
        return criteria.list();
    }
}
