package com.epam.esm.hibernate.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.persistence.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository repository;

    @BeforeEach
    public void createDefaultData() throws DaoException {
        for (int i = 0; i < 100; i++) {
            OrderEntity order = new OrderEntity();
            order.setIdUser((long) (Math.random() * 10 + 1));
            order.setIdCertificate((long) (Math.random() * 10 + 1));
            order.setCost((long) (Math.random() * 50 + 50));
            order.setClosed(false);
            repository.create(order);
        }
    }

    @Test
    public void testInsert() throws DaoException {
        OrderEntity order = new OrderEntity();
        order.setIdUser(1);
        order.setIdCertificate(1);
        order.setCost(100);
        order.setClosed(false);
        int result = repository.create(order);
        assertTrue(result > 0);
    }

    @Test
    public void findOrder() throws DaoException {
        OrderEntity order = new OrderEntity();
        order.setIdUser(1);
        order.setIdCertificate(1);
        order.setCost(100);
        order.setClosed(false);
        int result = repository.create(order);
        Optional<OrderEntity> entity = repository.find(result);
        assertTrue(entity.isPresent());
        assertEquals(result, entity.get().getId());
    }

    @Test
    public void findNotExistOrder() throws DaoException {
        Optional<OrderEntity> entity = repository.find(0);
        assertFalse(entity.isPresent());
    }

    @Test
    public void findOrders() throws DaoException {
        List<OrderEntity> entities = repository.findAll(10, 3);
        assertNotNull(entities);
        assertEquals(10, entities.size());
        assertEquals(21, entities.get(0).getId());
        assertEquals(30, entities.get(entities.size() - 1).getId());
    }

    @Test
    public void findOrdersOfSpecificUser() throws DaoException {
        int expectedIdUser = 3;
        OrderEntity order = new OrderEntity();
        order.setIdUser(expectedIdUser);
        order.setIdCertificate((long) (Math.random() * 10 + 1));
        order.setCost((long) (Math.random() * 50 + 50));
        order.setClosed(false);
        repository.create(order);
        List<OrderEntity> orders = repository.findOrdersOfSpecificUser(expectedIdUser, 10, 1);
        assertNotNull(orders);
        assertNotNull(orders.get(0));
        assertTrue(orders.stream().allMatch(entity -> entity.getIdUser() == expectedIdUser));
    }

    @Test
    public void findOrderOfSpecificUser() throws DaoException {
        int expectedIdUser = 3;
        OrderEntity order = new OrderEntity();
        order.setIdUser(expectedIdUser);
        order.setIdCertificate((long) (Math.random() * 10 + 1));
        order.setCost((long) (Math.random() * 50 + 50));
        order.setClosed(false);
        int idNewOrder = repository.create(order);
        List<OrderEntity> orders = repository.findOrderOfSpecificUser(expectedIdUser, idNewOrder);
        assertNotNull(orders);
        assertNotNull(orders.get(0));
        assertEquals(1, orders.size());
        assertEquals(idNewOrder, orders.get(0).getId());
        assertEquals(expectedIdUser, orders.get(0).getIdUser());
    }
}