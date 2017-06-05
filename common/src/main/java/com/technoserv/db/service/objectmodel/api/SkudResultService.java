package com.technoserv.db.service.objectmodel.api;

import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.service.Service;

import java.util.List;

/**
 * Created by 90630 on 14.12.2016.
 */
public interface SkudResultService extends Service<Long, SkudResult> {

    List<SkudResult> findAll();

}
