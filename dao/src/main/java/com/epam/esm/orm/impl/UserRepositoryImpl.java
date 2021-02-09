package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.HibernateUserEntity;
import com.epam.esm.orm.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private SessionFactory factory;

    @Override
    public List<HibernateUserEntity> find(long id) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            HibernateUserEntity user = (HibernateUserEntity) session.get(HibernateUserEntity.class, id);
            transaction.commit();
            return Collections.singletonList(user);
        } catch (Throwable e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateUserEntity> findAll(int limit, int page) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()){
            transaction = session.beginTransaction();
            List users = session.createQuery("from HibernateUserEntity")
                    .setFirstResult(limit * (page-1))
                    .setMaxResults(limit)
                    .getResultList();
            transaction.commit();
            return users;
        } catch (Throwable e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }
}
