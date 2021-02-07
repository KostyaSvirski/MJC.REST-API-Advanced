package com.epam.esm;

import com.epam.esm.exception.DaoException;

import java.util.List;

public interface Repository<T> {

    List<T> find(long id) throws DaoException;

    List<T> findAll(int limit, int page) throws DaoException;


}
