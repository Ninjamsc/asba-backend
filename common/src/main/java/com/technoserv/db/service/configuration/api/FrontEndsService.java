package com.technoserv.db.service.configuration.api;


import com.technoserv.db.model.configuration.FrontEnd;
import com.technoserv.db.service.Service;

/**
 * Created by sergey on 15.11.2016.
 */
public interface FrontEndsService extends Service<Long,FrontEnd> {

    FrontEnd findByUuid(String uuid);
    FrontEnd findByWorkstationName(String workstationName);
}
