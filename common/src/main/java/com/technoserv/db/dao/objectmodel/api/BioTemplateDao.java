package com.technoserv.db.dao.objectmodel.api;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.Document;

import java.util.List;

/**
 * Created by sergey on 23.11.2016.
 */
public interface BioTemplateDao extends Dao<Long, BioTemplate> {
    List<BioTemplate> getAllByDocument(Document d);
}
