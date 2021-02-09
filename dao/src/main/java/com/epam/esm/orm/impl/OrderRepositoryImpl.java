package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.OrderRepository;
import com.epam.esm.persistence.HibernateOrderEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private SessionFactory factory;

    @Override
    public List<HibernateOrderEntity> find(long id) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            HibernateOrderEntity order = session.get(HibernateOrderEntity.class, id);
            transaction.commit();
            return Collections.singletonList(order);
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateOrderEntity> findAll(int limit, int page) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List orders = session.createQuery("from HibernateOrderEntity ")
                    .setFirstResult(limit * (page-1))
                    .setMaxResults(limit)
                    .getResultList();
            transaction.commit();
            return orders;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateOrderEntity> findOrderOfSpecificUser(long idUser, long idOrder) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Query query = session
                    .createQuery("from HibernateOrderEntity o" +
                            " where o.idUser =: idUser and o.id =: idOrder");
            query.setParameter(Math.toIntExact(idUser), idUser);
            query.setParameter(Math.toIntExact(idOrder), idOrder);
            List orders = query.getResultList();
            transaction.commit();
            return orders;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public int create(HibernateOrderEntity newOrder) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Integer idOfNewOrder = (Integer) session.save(newOrder);
            transaction.commit();
            return idOfNewOrder;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateOrderEntity> findOrdersOfSpecificUser(long idUser, int limit, int page)
            throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Query query = session
                    .createQuery("from HibernateOrderEntity o" +
                            " where o.idUser =: idUser");
            query.setParameter(Math.toIntExact(idUser), idUser);
            List orders = query.setFirstResult(limit * (page-1))
                    .setMaxResults(limit)
                    .getResultList();
            transaction.commit();
            return orders;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }
}
