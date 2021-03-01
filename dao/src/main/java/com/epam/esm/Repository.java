package com.epam.esm;

import com.epam.esm.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    Optional<T> find(long id);

    List<T> findAll(int limit, int page);


}
