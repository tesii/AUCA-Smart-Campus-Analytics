package com.example.zk.config;

import javax.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;

@Configuration
public class ZkConfig {

    @Bean
    public ServletRegistrationBean<DHtmlLayoutServlet> zkLayoutServlet() {
        ServletRegistrationBean<DHtmlLayoutServlet> bean =
            new ServletRegistrationBean<>(new DHtmlLayoutServlet(), "*.zul");
        bean.addInitParameter("update-uri", "/zkau");
        bean.setLoadOnStartup(1);
        bean.setName("zkLoader");
        return bean;
    }

    @Bean
    public ServletRegistrationBean<DHtmlUpdateServlet> zkUpdateServlet() {
        ServletRegistrationBean<DHtmlUpdateServlet> bean =
            new ServletRegistrationBean<>(new DHtmlUpdateServlet(), "/zkau/*");
        bean.setLoadOnStartup(2);
        bean.setName("auEngine");
        return bean;
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> zkSessionListener() {
        return new ServletListenerRegistrationBean<>(new HttpSessionListener());
    }
}