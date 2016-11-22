package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.BPMReportDao;
import com.technoserv.db.model.objectmodel.BPMReport;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("bpmReportDao")
public class BPMReportDaoImpl extends AbstractHibernateDao<Long,BPMReport> implements BPMReportDao {
}
