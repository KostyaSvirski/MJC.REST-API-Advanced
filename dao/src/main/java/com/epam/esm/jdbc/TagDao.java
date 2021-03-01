package com.epam.esm.jdbc;

import com.epam.esm.Repository;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;


public interface TagDao extends Repository<Tag> {

    int create(Tag entity) throws DaoException;

    void delete(long id) throws DaoException;
}
