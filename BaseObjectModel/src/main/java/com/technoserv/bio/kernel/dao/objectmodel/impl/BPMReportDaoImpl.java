package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.BPMReportDao;
import com.technoserv.bio.kernel.model.objectmodel.BPMReport;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository
public class BPMReportDaoImpl extends AbstractHibernateDao<Long,BPMReport> implements BPMReportDao {
}
