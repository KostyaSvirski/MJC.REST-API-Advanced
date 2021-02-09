package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.TagRepository;
import com.epam.esm.persistence.HibernateTagEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    @Autowired
    private SessionFactory factory;

    @Override
    public List<HibernateTagEntity> find(long id) throws DaoException {
        Transaction transaction = null;
        try(Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List tags = session
                    .createQuery("from HibernateTagEntity tag where tag.id = id")
                    .getResultList();
            transaction.commit();
            return tags;
        } catch (Throwable e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateTagEntity> findAll(int limit, int page) throws DaoException {
        Transaction transaction = null;
        try(Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List tags = session
                    .createQuery("from HibernateTagEntity tag")
                    .setFirstResult(limit * (page-1))
                    .setMaxResults(limit).getResultList();
            transaction.commit();
            return tags;
        } catch (Throwable e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public int create(HibernateTagEntity entity) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            session.save(entity);
            int idOfNewTag = session
                    .createQuery("select max(h.id) from HibernateTagEntity h")
                    .getFirstResult();
            transaction.commit();
            return idOfNewTag;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }

    }

    @Override
    public void delete(long id) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction =  session.beginTransaction();
            HibernateTagEntity tagToDelete = session.get(HibernateTagEntity.class, id);
            session.delete(tagToDelete);
            transaction.commit();
        } catch (Throwable e) {
            if(transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }
}
