package com.technoserv.db.dao.configuration.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.configuration.api.SystemSettingDao;
import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import org.springframework.stereotype.Repository;

/**
 * Created by 90630 on 14.12.2016.
 */
@Repository("systemSettingDao")
public class SystemSettingDaoImpl extends AbstractHibernateDao<SystemSettingsType,SystemSettings> implements SystemSettingDao {
}
