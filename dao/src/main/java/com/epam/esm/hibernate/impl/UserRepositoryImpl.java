package com.epam.esm.hibernate.impl;

import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.persistence.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final static String HQL_FIND_ALL = "from UserEntity order by id";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<UserEntity> find(long id) {
        return Optional.ofNullable(em.find(UserEntity.class, id));
    }

    @Override
    public List<UserEntity> findAll(int limit, int page) {
        return em.createQuery(HQL_FIND_ALL)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }
}
