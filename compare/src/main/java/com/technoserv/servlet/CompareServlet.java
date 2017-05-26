package com.technoserv.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import java.io.IOException;

@Transactional
public class CompareServlet extends GenericServlet implements Servlet {

    private static final long serialVersionUID = 2533800815776098302L;

    private static final Logger log = LoggerFactory.getLogger(CompareServlet.class);

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
        log.debug("service");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("Compare initialized successfully.");
    }
}
