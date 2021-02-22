package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    List<T> findAll(int limit, int page);

    Optional<T> find(long id);



}
