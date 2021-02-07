package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/database.properties")
@ComponentScan("com.epam.esm")
public class ConfigDB {

    @Value("${db.driverClassName}")
    private String driverClassName;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String userName;
    @Value("${db.password}")
    private String pass;
    @Value("${db.maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;
    @Value("${db.autoReconnect}")
    private String autoReconnect;
    @Value("${db.characterEncoding}")
    private String characterEncoding;
    @Value("${db.initialSize}")
    private int initialSize;
    @Value("${db.serverTimezone}")
    private String serverTimeZone;
    @Value("${db.useUnicode}")
    private String useUnicode;

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(userName);
        dataSource.setPassword(pass);
        dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        dataSource.setInitialSize(initialSize);
        dataSource.addConnectionProperty("characterEncoding", characterEncoding);
        dataSource.addConnectionProperty("autoReconnect", autoReconnect);
        dataSource.addConnectionProperty("serverTimezone", serverTimeZone);
        dataSource.addConnectionProperty("useUnicode", useUnicode);
        return dataSource;
    }
}
