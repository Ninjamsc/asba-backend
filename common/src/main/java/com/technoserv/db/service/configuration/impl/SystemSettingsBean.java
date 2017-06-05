package com.technoserv.db.service.configuration.impl;

import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.api.SystemSettingService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 90630 on 14.12.2016.
 */
@Component
@DependsOn(value = "initialDataBuilder")
public class SystemSettingsBean implements InitializingBean {

    @Autowired
    private SystemSettingService systemSettingService;

    private Map<SystemSettingsType, SystemSettings> systemSettingsCache = new HashMap<SystemSettingsType, SystemSettings>();

    private Date nextReloadDate;

    private static final int AMOUNT_TTL = 1; //Колличество прибавляемых единиц

    private static final int FIELD_TTL = Calendar.MINUTE; //Единицап измерения времени


    public void afterPropertiesSet() throws Exception {
        systemSettingsCache = new HashMap<SystemSettingsType, SystemSettings>();
        reloadCache();
    }

    public void reloadCache() {
        systemSettingsCache.clear();
        List<SystemSettings> all = systemSettingService.getAll();
        for (SystemSettings systemSettings : all) {
            systemSettingsCache.put(systemSettings.getId(), systemSettings);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(FIELD_TTL, AMOUNT_TTL);
        nextReloadDate = calendar.getTime();
    }

    public String get(SystemSettingsType systemSettingsType) {
        if (new Date().after(nextReloadDate) || systemSettingsCache.isEmpty()) {
            reloadCache();
        }
        return systemSettingsCache.get(systemSettingsType).getValue();
    }
}
