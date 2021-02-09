package com.epam.esm.orm;

import com.epam.esm.Repository;
import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.HibernateOrderEntity;

import java.util.List;

public interface OrderRepository extends Repository<HibernateOrderEntity> {

    List<HibernateOrderEntity> findOrderOfSpecificUser(long idUser, long idOrder)
            throws DaoException;

    int create(HibernateOrderEntity newOrder) throws DaoException;

    List<HibernateOrderEntity> findOrdersOfSpecificUser(long idUser, int limit, int page)
            throws DaoException;

}
