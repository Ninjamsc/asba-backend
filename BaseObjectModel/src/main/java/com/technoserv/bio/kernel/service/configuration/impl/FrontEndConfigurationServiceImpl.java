package com.technoserv.bio.kernel.service.configuration.impl;

import com.technoserv.bio.kernel.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.configuration.api.FrontEndConfigurationService;
import org.springframework.stereotype.Service;


/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class FrontEndConfigurationServiceImpl extends AbstractService<Long, FrontEndConfiguration,FrontEndConfigurationDao> implements FrontEndConfigurationService {


}
