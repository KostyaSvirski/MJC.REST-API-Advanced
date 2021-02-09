package com.epam.esm;

import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import com.epam.esm.persistence.HibernateOrderEntity;
import com.epam.esm.persistence.HibernateTagEntity;
import com.epam.esm.persistence.HibernateUserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/database_test.properties")
@ComponentScan("com.epam.esm")
public class ConfigTestDB {

    @Value("${test.hibernate.connection.url}")
    private String url;
    @Value("${test.hibernate.connection.username}")
    private String name;
    @Value("${test.hibernate.connection.password}")
    private String password;
    @Value("${test.hibernate.connection.driver_class}")
    private String driver;
    @Value("${test.hibernate.show-sql}")
    private String showSql;
    @Value("${test.hibernate.hbm2ddl.auto}")
    private String ddlAuto;
    @Value("${test.hibernate.dialect}")
    private String dialect;

    @Bean
    public SessionFactory session() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.driver_class", driver);
        configuration.setProperty("hibernate.connection.username", name);
        configuration.setProperty("hibernate.connection.password", password);
        configuration.setProperty("hibernate.dialect", dialect);
        configuration.setProperty("hibernate.show_sql", showSql);
        configuration.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        configuration.addAnnotatedClass(HibernateGiftCertificateEntity.class)
                .addAnnotatedClass(HibernateTagEntity.class)
                .addAnnotatedClass(HibernateUserEntity.class)
                .addAnnotatedClass(HibernateOrderEntity.class);
        ServiceRegistry service = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(service);
    }


}
