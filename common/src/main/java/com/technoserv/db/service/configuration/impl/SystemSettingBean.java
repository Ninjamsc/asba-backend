package com.technoserv.db.service.configuration.impl;

import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.api.SystemSettingService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 90630 on 14.12.2016.
 */
@Component
public class SystemSettingBean implements InitializingBean {

    @Autowired
    private SystemSettingService systemSettingService;


    private Map<SystemSettingsType,SystemSettings> systemSettingsCache;


    public void afterPropertiesSet() throws Exception {
        systemSettingsCache = new HashMap<SystemSettingsType, SystemSettings>();
        reloadCache();
    }

    public void reloadCache() {
        List<SystemSettings> all = systemSettingService.getAll();
        systemSettingsCache.clear();
        for (SystemSettings systemSettings : all) {
            systemSettingsCache.put(systemSettings.getId(),systemSettings);
        }
    }

    public String get(SystemSettingsType systemSettingsType) {
        return systemSettingsCache.get(systemSettingsType).getValue();
    }
}
