package com.epam.esm.config;

import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import com.epam.esm.persistence.HibernateOrderEntity;
import com.epam.esm.persistence.HibernateTagEntity;
import com.epam.esm.persistence.HibernateUserEntity;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
@PropertySource("classpath:/database.properties")
@ComponentScan("com.epam.esm")
public class    ConfigDB {

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

    @Qualifier("product")
    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return Persistence
                .createEntityManagerFactory("gift-certificates-api");
    }

    @Qualifier("test")
    @Bean
    public EntityManagerFactory entityManagerFactoryTest() {
        return Persistence
                .createEntityManagerFactory("gift-certificates-test");
    }
}
