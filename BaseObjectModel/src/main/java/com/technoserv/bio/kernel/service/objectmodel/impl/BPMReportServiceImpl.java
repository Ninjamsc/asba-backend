package com.technoserv.bio.kernel.service.objectmodel.impl;

import com.technoserv.bio.kernel.dao.objectmodel.api.BPMReportDao;
import com.technoserv.bio.kernel.model.objectmodel.BPMReport;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.objectmodel.api.BPMReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class BPMReportServiceImpl extends AbstractService<Long, BPMReport,BPMReportDao> implements BPMReportService {
    @Override
    @Autowired
    @Qualifier("bpmReportDao")
    public void setDao(BPMReportDao dao) {
        this.dao = dao;
    }
}
