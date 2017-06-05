package com.technoserv.db.dao.configuration.api;


import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.configuration.FrontEnd;

/**
 * Created by sergey on 15.11.2016.
 */
public interface FrontEndsDao extends Dao<Long, FrontEnd> {

    FrontEnd findByUuid(String uuid);

    FrontEnd findByWorkstationName(String workstationName);
}
