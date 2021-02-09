package com.epam.esm.orm;

import com.epam.esm.Repository;
import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.HibernateTagEntity;

public interface TagRepository extends Repository<HibernateTagEntity> {

    int create(HibernateTagEntity entity) throws DaoException;

    void delete(long id) throws DaoException;
}
