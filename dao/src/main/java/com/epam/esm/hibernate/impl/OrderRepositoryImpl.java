package com.epam.esm.hibernate.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.persistence.OrderEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String HQL_FIND_ALL = "select o from OrderEntity o order by o.id";
    private static final String SUFFIX_FOR_ONE_ORDER = " and o.id =: idOrder";
    private static final String HQL_FIND_ORDERS_OF_USER = "from OrderEntity o" +
            " where o.idUser =: idUser";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<OrderEntity> find(long id) {
        return Optional.ofNullable(em.find(OrderEntity.class, id));
    }

    @Override
    public List<OrderEntity> findAll(int limit, int page) throws DaoException {
        List orders = em.createQuery(HQL_FIND_ALL)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
        return orders;

    }

    @Override
    public List<OrderEntity> findOrderOfSpecificUser(long idUser, long idOrder) {
        Query query = em.createQuery(HQL_FIND_ORDERS_OF_USER + SUFFIX_FOR_ONE_ORDER);
        query.setParameter("idUser", idUser);
        query.setParameter("idOrder", idOrder);
        List orders = query.getResultList();
        return orders;
    }

    @Override
    public int create(OrderEntity newOrder) throws DaoException {
        em.persist(newOrder);
        int idNewOrder = (int) newOrder.getId();
        return idNewOrder;
    }

    @Override
    public List<OrderEntity> findOrdersOfSpecificUser(long idUser, int limit, int page) {
        Query query = em.createQuery(HQL_FIND_ORDERS_OF_USER);
        query.setParameter("idUser", idUser);
        List orders = query.setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
        return orders;
    }
}
