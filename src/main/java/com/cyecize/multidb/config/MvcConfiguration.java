package com.cyecize.multidb.config;

import com.cyecize.multidb.areas.conn.interceptors.DatabaseConnectionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    private final DatabaseConnectionInterceptor databaseConnectionInterceptor;

    @Autowired
    public MvcConfiguration(DatabaseConnectionInterceptor databaseConnectionInterceptor) {
        this.databaseConnectionInterceptor = databaseConnectionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.databaseConnectionInterceptor);
    }
}
