package com.epam.esm.hibernate;

import com.epam.esm.Repository;
import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.TagEntity;
import org.springframework.transaction.annotation.Transactional;

public interface TagRepository extends Repository<TagEntity> {

    @Transactional
    int create(TagEntity entity) throws DaoException;

    @Transactional
    void delete(long id) throws DaoException;
}
