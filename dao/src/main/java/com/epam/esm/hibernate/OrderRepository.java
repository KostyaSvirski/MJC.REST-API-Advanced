package com.epam.esm.hibernate;

import com.epam.esm.Repository;
import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.OrderEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends Repository<OrderEntity> {

    List<OrderEntity> findOrderOfSpecificUser(long idUser, long idOrder)
            throws DaoException;

    @Transactional
    int create(OrderEntity newOrder) throws DaoException;

    List<OrderEntity> findOrdersOfSpecificUser(long idUser, int limit, int page)
            throws DaoException;

}
