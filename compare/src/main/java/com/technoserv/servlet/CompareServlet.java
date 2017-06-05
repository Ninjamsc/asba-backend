package com.technoserv.servlet;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import java.io.IOException;

@Transactional
public class CompareServlet extends GenericServlet implements Servlet {

    /**
     *
     */
    private static final long serialVersionUID = 2533800815776098302L;

    @Autowired
    protected SessionFactory sessionFactory;

    @Override
    public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
        // TODO Auto-generated method stub
        System.out.println("!!!!!!");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        //this.config = config;
        System.out.println("----------");
        System.out.println("---------- Compare Initialized successfully ----------");
        System.out.println("----------");
        //InitialContext ctx = new InitialContext();
        //Context initCtx  = (Context) ctx.lookup("java:/comp/env");
        //DataSource ds = (DataSource) initCtx.lookup("jdbc/MyLocalDB");
        //WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }
}
