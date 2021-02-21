package com.epam.esm.hibernate.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.TagRepository;
import com.epam.esm.persistence.TagEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private final static String HQL_FIND_ALL = "from TagEntity order by id";
    private final static String HQL_FIND_LAST_ID = "select max(tag.id) from HibernateTagEntity tag";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<TagEntity> find(long id) {
        return Optional.ofNullable(em.find(TagEntity.class, id));
    }

    @Override
    public List<TagEntity> findAll(int limit, int page) {
        return em.createQuery(HQL_FIND_ALL)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public int create(TagEntity entity) {
        em.persist(entity);
        return (int) entity.getId();
    }

    @Override
    public void delete(long id) {
        TagEntity tagToDelete = em.find(TagEntity.class, id);
        em.remove(tagToDelete);
    }
}
