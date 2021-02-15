package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.OrderRepository;
import com.epam.esm.persistence.HibernateOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final String HQL_FIND_ALL = "FROM HibernateOrderEntity ORDER BY id";
    private static final String SUFFIX_FOR_ONE_ORDER = " and o.id =: idOrder";
    private static final String HQL_FIND_LAST_ID = "select max(o.id) from HibernateOrderEntity o";
    private static final String HQL_FIND_ORDERS_OF_USER = "from HibernateOrderEntity o" +
            " where o.idUser =: idUser";

    @Qualifier("test")
    @Autowired
    private EntityManagerFactory factory;

    @Override
    public List<HibernateOrderEntity> find(long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            HibernateOrderEntity order = em.find(HibernateOrderEntity.class, id);
            if(order != null) {
                em.detach(order);
            }
            return Collections.singletonList(order);
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<HibernateOrderEntity> findAll(int limit, int page) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            List orders = em.createQuery(HQL_FIND_ALL)
                    .setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
            return orders;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<HibernateOrderEntity> findOrderOfSpecificUser(long idUser, long idOrder) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            Query query = em.createQuery(HQL_FIND_ORDERS_OF_USER + SUFFIX_FOR_ONE_ORDER);
            query.setParameter("idUser", idUser);
            query.setParameter("idOrder", idOrder);
            List orders = query.getResultList();
            return orders;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int create(HibernateOrderEntity newOrder) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            em.getTransaction().begin();
            em.persist(newOrder);
            /*int idNewOrder = (int) em.createQuery(HQL_FIND_LAST_ID).getResultList().get(0);*/
            int idNewOrder = (int) newOrder.getId();
            em.getTransaction().commit();
            return idNewOrder;
        } catch (Throwable e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<HibernateOrderEntity> findOrdersOfSpecificUser(long idUser, int limit, int page)
            throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            Query query = em.createQuery(HQL_FIND_ORDERS_OF_USER);
            query.setParameter("idUser", idUser);
            List orders = query.setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
            return orders;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
