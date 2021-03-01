package com.epam.esm.jdbc;

import com.epam.esm.Repository;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.DaoException;

import java.util.List;

public interface OrderDao extends Repository<Order> {

    List<Order> findOrderOfSpecificUser(long idUser, long idOrder) throws DaoException;

    int create(Order newOrder) throws DaoException;

    List<Order> findOrdersOfSpecificUser(long idUser, int limit, int page) throws DaoException;

}
