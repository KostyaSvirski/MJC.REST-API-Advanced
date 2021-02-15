package com.epam.esm.orm.impl;

import com.epam.esm.config.ConfigDB;
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
        classes = {ConfigDB.class},
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
        assertEquals(101, result);
    }

    @Test
    public void findOrder() throws DaoException {
        HibernateOrderEntity order = new HibernateOrderEntity();
        order.setIdUser(1);
        order.setIdCertificate(1);
        order.setCost(100);
        order.setClosed(false);
        int result = repository.create(order);
        List<HibernateOrderEntity> entities = repository.find(result);
        assertNotNull(entities);
        assertNotNull(entities.get(0));
        assertEquals(result, entities.get(0).getId());
    }

    @Test
    public void findNotExistOrder() throws DaoException {
        List<HibernateOrderEntity> entities = repository.find(101);
        assertNotNull(entities);
        assertNull(entities.get(0));
    }

    @Test
    public void findOrders() throws DaoException {
        List<HibernateOrderEntity> entities = repository.findAll(10, 3);
        assertNotNull(entities);
        assertEquals(10, entities.size());
        assertEquals(21, entities.get(0).getId());
        assertEquals(30, entities.get(entities.size() - 1).getId());
    }

    @Test
    public void findOrdersOfSpecificUser() throws DaoException {
        int expectedIdUser = 3;
        HibernateOrderEntity order = new HibernateOrderEntity();
        order.setIdUser(expectedIdUser);
        order.setIdCertificate((long) (Math.random() * 10 + 1));
        order.setCost((long) (Math.random() * 50 + 50));
        order.setClosed(false);
        repository.create(order);
        List<HibernateOrderEntity> orders = repository.findOrdersOfSpecificUser(expectedIdUser, 10, 1);
        assertNotNull(orders);
        assertNotNull(orders.get(0));
        assertTrue(orders.stream().allMatch(entity -> entity.getIdUser() == expectedIdUser));
    }

    @Test
    public void findOrderOfSpecificUser() throws DaoException {
        int expectedIdUser = 3;
        HibernateOrderEntity order = new HibernateOrderEntity();
        order.setIdUser(expectedIdUser);
        order.setIdCertificate((long) (Math.random() * 10 + 1));
        order.setCost((long) (Math.random() * 50 + 50));
        order.setClosed(false);
        int idNewOrder = repository.create(order);
        List<HibernateOrderEntity> orders = repository.findOrderOfSpecificUser(expectedIdUser, idNewOrder);
        assertNotNull(orders);
        assertNotNull(orders.get(0));
        assertEquals(1, orders.size());
        assertEquals(idNewOrder, orders.get(0).getId());
        assertEquals(expectedIdUser, orders.get(0).getIdUser());
    }
}