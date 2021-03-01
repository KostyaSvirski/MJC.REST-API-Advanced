package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

public interface TagService extends BaseService<TagDTO> {

    int create(TagDTO bean);

    @Transactional
    void delete(long id) throws ServiceException;
}
