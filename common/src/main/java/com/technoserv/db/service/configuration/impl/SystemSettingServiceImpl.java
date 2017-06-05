package com.technoserv.db.service.configuration.impl;

import com.technoserv.db.dao.configuration.api.SystemSettingDao;
import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.configuration.api.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by 90630 on 14.12.2016.
 */
@Service
public class SystemSettingServiceImpl extends AbstractService<SystemSettingsType, SystemSettings, SystemSettingDao>
        implements SystemSettingService {

    @Override
    @Autowired
    @Qualifier("systemSettingDao")
    public void setDao(SystemSettingDao dao) {
        this.dao = dao;
    }

}
