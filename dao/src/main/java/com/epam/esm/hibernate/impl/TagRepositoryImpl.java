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
    public Optional<TagEntity> find(long id) throws DaoException {
        return Optional.of(em.find(TagEntity.class, id));
    }

    @Override
    public List<TagEntity> findAll(int limit, int page) throws DaoException {
        try {
            List tags = em.createQuery(HQL_FIND_ALL)
                    .setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
            return tags;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public int create(TagEntity entity) throws DaoException {
        try {
            em.persist(entity);
            int idNewTag = (int) entity.getId();
            return idNewTag;
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        try {
            TagEntity tagToDelete = em.find(TagEntity.class, id);
            em.remove(tagToDelete);
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        }
    }
}
