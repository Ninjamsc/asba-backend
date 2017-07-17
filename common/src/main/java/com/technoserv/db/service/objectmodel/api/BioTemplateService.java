package com.technoserv.db.service.objectmodel.api;

import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.service.Service;

import java.util.List;

/**
 * Created by sergey on 23.11.2016.
 */
public interface BioTemplateService extends Service<Long, BioTemplate> {
    List<BioTemplate> getAllByDocument(Document d);
}
