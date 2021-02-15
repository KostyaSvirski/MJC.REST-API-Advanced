package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.UserRepository;
import com.epam.esm.persistence.HibernateUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final static String HQL_FIND_ALL = "from HibernateUserEntity order by id";

    @Qualifier("test")
    @Autowired
    private EntityManagerFactory factory;

    @Override
    public List<HibernateUserEntity> find(long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            HibernateUserEntity user = em.find(HibernateUserEntity.class, id);
            em.detach(user);
            return Collections.singletonList(user);
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<HibernateUserEntity> findAll(int limit, int page) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            List listOfUsers = em.createQuery(HQL_FIND_ALL)
                    .setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
            return listOfUsers;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
