package com.epam.esm.config;

import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import com.epam.esm.persistence.HibernateOrderEntity;
import com.epam.esm.persistence.HibernateTagEntity;
import com.epam.esm.persistence.HibernateUserEntity;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/database.properties")
@ComponentScan("com.epam.esm")
public class ConfigDB {

    @Value("${hibernate.connection.driver_class}")
    private String driverClassName;
    @Value("${hibernate.connection.url}")
    private String url;
    @Value("${hibernate.connection.username}")
    private String userName;
    @Value("${hibernate.connection.password}")
    private String pass;
    @Value("${db.maxOpenPreparedStatements}")
    private int maxOpenPreparedStatements;
    @Value("${db.autoReconnect}")
    private String autoReconnect;
    @Value("${hibernate.connection.characterEncoding}")
    private String characterEncoding;
    @Value("${hibernate.connection.pool_size}")
    private String initialSize;
    @Value("${db.serverTimezone}")
    private String serverTimeZone;
    @Value("${hibernate.connection.useUnicode}")
    private String useUnicode;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.current_session_context_class}")
    private String sessionContextClass;

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(userName);
        dataSource.setPassword(pass);
        dataSource.setMaxOpenPreparedStatements(maxOpenPreparedStatements);
        dataSource.setInitialSize(Integer.parseInt(initialSize));
        dataSource.addConnectionProperty("characterEncoding", characterEncoding);
        dataSource.addConnectionProperty("autoReconnect", autoReconnect);
        dataSource.addConnectionProperty("serverTimezone", serverTimeZone);
        dataSource.addConnectionProperty("useUnicode", useUnicode);
        return dataSource;
    }

    @Bean
    public SessionFactory session() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.driver_class", driverClassName);
        configuration.setProperty("hibernate.connection.username", userName);
        configuration.setProperty("hibernate.connection.password", pass);
        configuration.setProperty("hibernate.connection.pool_size", initialSize);
        configuration.setProperty("hibernate.connection.characterEncoding", characterEncoding);
        configuration.setProperty("hibernate.dialect", dialect);
        configuration.setProperty("hibernate.show_sql", showSql);
        configuration.setProperty("hibernate.current_session_context_class", sessionContextClass);
        configuration.addAnnotatedClass(HibernateGiftCertificateEntity.class)
                .addAnnotatedClass(HibernateOrderEntity.class)
                .addAnnotatedClass(HibernateTagEntity.class)
                .addAnnotatedClass(HibernateUserEntity.class);
        ServiceRegistry service = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(service);
    }
}
