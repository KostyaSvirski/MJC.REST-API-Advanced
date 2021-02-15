package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.TagRepository;
import com.epam.esm.persistence.HibernateTagEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final static String HQL_FIND_ALL = "from HibernateTagEntity order by id";
    private final static String HQL_FIND_LAST_ID = "select max(tag.id) from HibernateTagEntity tag";

    @Qualifier("test")
    @Autowired
    private EntityManagerFactory factory;

    @Override
    public List<HibernateTagEntity> find(long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            HibernateTagEntity tag = em.find(HibernateTagEntity.class, id);
            if (tag != null) {
                em.detach(tag);
            }
            return Collections.singletonList(tag);
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<HibernateTagEntity> findAll(int limit, int page) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            List tags = em.createQuery(HQL_FIND_ALL)
                    .setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
            return tags;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int create(HibernateTagEntity entity) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            int idNewTag = (int) entity.getId();
            em.getTransaction().commit();
            return idNewTag;
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
    public void delete(long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            em.getTransaction().begin();
            HibernateTagEntity tagToDelete = em.find(HibernateTagEntity.class, id);
            em.remove(tagToDelete);
            em.getTransaction().commit();
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
}
