package com.epam.esm;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends Repository<Order> {

    List<Order> findOrderOfSpecificUser(long idUser, long idOrder, int limit, int page) throws DaoException;

    int create(Order newOrder) throws DaoException;

    List<Order> findOrdersOfSpecificUser(long idUser, int limit, int page) throws DaoException;

}
