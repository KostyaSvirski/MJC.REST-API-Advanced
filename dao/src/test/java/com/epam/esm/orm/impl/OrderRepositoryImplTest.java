package com.epam.esm.orm.impl;

import com.epam.esm.ConfigTestDB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.OrderRepository;
import com.epam.esm.persistence.HibernateOrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigTestDB.class},
        loader = AnnotationConfigContextLoader.class)
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository repository;

    @BeforeEach
    public void defaultData() throws DaoException {
        for (int i = 0; i < 100; i++) {
            HibernateOrderEntity order = new HibernateOrderEntity();
            order.setIdUser((long) (Math.random() * 10 + 1));
            order.setIdCertificate((long) (Math.random() * 10 + 1));
            order.setCost((long) (Math.random() * 50 + 50));
            order.setClosed(false);
            repository.create(order);
        }
    }

    @Test
    public void testInsert() throws DaoException {
        HibernateOrderEntity order = new HibernateOrderEntity();
        order.setIdUser(1);
        order.setIdCertificate(1);
        order.setCost(100);
        order.setClosed(false);
        int result = repository.create(order);
        assertNotNull(result);
    }

    @Test
    public void findOrder() throws DaoException {
        HibernateOrderEntity order = new HibernateOrderEntity();
        order.setIdUser(1);
        order.setIdCertificate(1);
        order.setCost(100);
        order.setClosed(false);
        int result = repository.create(order);
        assertNotNull(result);
        List<HibernateOrderEntity> entities = repository.find(result);
        assertNotNull(entities);
        assertNotNull(entities.get(0));
    }

    @Test
    public void findNotExistOrder() throws DaoException {
        List<HibernateOrderEntity> entities = repository.find(100);
        assertNotNull(entities);
        assertTrue(entities.get(0) == null);
    }

    @Test
    public void findOrders() throws DaoException {
        List<HibernateOrderEntity> entities = repository.findAll(10, 3);
        assertNotNull(entities);
        assertEquals(10, entities.size());
        assertEquals(21, entities.get(0).getId());
        assertEquals(30, entities.get(entities.size()-1).getId());
    }




}