package com.technoserv.db.dao.objectmodel.api;


import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.objectmodel.Request;

import java.util.Collection;

/**
 * Created by sergey on 15.11.2016.
 */
public interface RequestDao extends Dao<Long, Request> {
    Request findByOrderNumber(Long id);
    Collection<Request> findNotProcessed();
    Collection<Request> findByIin(Long id);
}
