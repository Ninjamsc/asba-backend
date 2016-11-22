package com.technoserv.bio.kernel.scheduler;

import com.technoserv.bio.kernel.RequestProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jvnet.hk2.annotations.Service;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * Created by Adrey on 22.11.2016.
 */
public class RequestProcessingJob extends QuartzJobBean {

    private static final Log logger = LogFactory.getLog(RequestProcessingJob.class);

    private int timeout;

    public RequestProcessingJob() {
    }

    @Autowired
    private RequestProcessor requestProcessor;

    /**
     * Setter called after the Job is instantiated
     * with the value from the JobDetailFactoryBean
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
        // do the actual work
        logger.debug("!!!!!!!" + new Date());
        System.out.println("SSsadfsdfsdf" + new Date());
    }

}