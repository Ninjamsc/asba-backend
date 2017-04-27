package com.technoserv.db.dao.objectmodel.api;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.SkudResult;

import java.util.List;

/**
 * Created by 90630 on 14.12.2016.
 */
public interface SkudResultDao extends Dao<Long, SkudResult> {
    List<SkudResult> findAll();
}
