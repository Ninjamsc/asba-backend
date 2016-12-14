package com.technoserv.db.service.configuration.impl;

import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.api.SystemSettingService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 90630 on 14.12.2016.
 */
@Component
public class SystemSettingsBean implements InitializingBean {

    @Autowired
    private SystemSettingService systemSettingService;


    private Map<SystemSettingsType,SystemSettings> systemSettingsCache;


    private Date nextReloadDate;

    private static final int AMOUNT_TTL = 1;//Колличество прибавляемых единиц
    private static final int FIELD_TTL = Calendar.MINUTE;//Единицап измерения времени



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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(FIELD_TTL, AMOUNT_TTL);
        nextReloadDate = calendar.getTime();

}

    public String get(SystemSettingsType systemSettingsType) {
        if(new Date().after(nextReloadDate)) {
            reloadCache();
        }
        return systemSettingsCache.get(systemSettingsType).getValue();
    }
}
