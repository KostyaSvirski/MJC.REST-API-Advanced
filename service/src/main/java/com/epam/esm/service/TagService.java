package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ServiceException;

public interface TagService extends BaseService<TagDTO> {

    int create(TagDTO bean);

    void delete(long id) throws ServiceException;
}
