package com.epam.esm.hibernate.impl;

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
    public List<OrderEntity> findAll(int limit, int page) {
        return em.createQuery(HQL_FIND_ALL)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();

    }

    @Override
    public List<OrderEntity> findOrderOfSpecificUser(long idUser, long idOrder) {
        Query query = em.createQuery(HQL_FIND_ORDERS_OF_USER + SUFFIX_FOR_ONE_ORDER);
        query.setParameter("idUser", idUser);
        query.setParameter("idOrder", idOrder);
        return query.getResultList();
    }

    @Override
    public int create(OrderEntity newOrder) {
        em.persist(newOrder);
        return (int) newOrder.getId();
    }

    @Override
    public List<OrderEntity> findOrdersOfSpecificUser(long idUser, int limit, int page) {
        Query query = em.createQuery(HQL_FIND_ORDERS_OF_USER);
        query.setParameter("idUser", idUser);
        return query.setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }
}
