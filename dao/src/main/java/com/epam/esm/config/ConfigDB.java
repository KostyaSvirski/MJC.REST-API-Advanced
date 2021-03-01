package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@PropertySource("classpath:/database.properties")
@ComponentScan("com.epam.esm")
@EnableTransactionManagement
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
    private int initialSize;
    @Value("${db.serverTimezone}")
    private String serverTimeZone;
    @Value("${hibernate.connection.useUnicode}")
    private String useUnicode;
    @Value("${hibernate.dialect}")
    private String dialect;
    @Value("${hibernate.show_sql}")
    private String showSql;
    @Value("${hibernate.hbm2ddl.auto}")
    private String auto;

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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaProperties(jpaProperties());
        factoryBean.setJpaVendorAdapter(vendorAdapter());
        factoryBean.setPackagesToScan("com.epam.esm");
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(this.entityManagerFactory().getObject());
        return transactionManager;

    }

    private Properties jpaProperties() {
        Properties propJpa = new Properties();
        propJpa.put("hibernate.hbm2ddl.auto", auto);
        propJpa.put("hibernate.show_sql", showSql);
        propJpa.put("hibernate.dialect", dialect);
        return propJpa;
    }

    private AbstractJpaVendorAdapter vendorAdapter() {
        AbstractJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(Boolean.getBoolean(showSql));
        vendorAdapter.setGenerateDdl(true);
        return vendorAdapter;
    }
}
