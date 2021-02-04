package com.epam.esm.service.impl;

import com.epam.esm.TagDao;
import com.epam.esm.converter.TagDTOToTagEntityConverter;
import com.epam.esm.converter.TagEntityToTagDTOConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.TagService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateTagLink;
import com.epam.esm.validator.realisation.tag.TagNameValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagEntityToTagDTOConverter converterToDTO;
    @Autowired
    private TagDTOToTagEntityConverter converterToEntity;
    private TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagDTO> findAll(int limit, int page) throws ServiceException {
        try {
            List<Tag> listOfEntities = tagDao.findAll(limit, page);
            return listOfEntities.stream()
                    .map(tag -> converterToDTO.apply(tag))
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public Optional<TagDTO> find(long id, int limit, int page) throws ServiceException {
        try {
            List<Tag> listFromDao = tagDao.find(id, limit, page);
            Optional<TagDTO> tagToFind = listFromDao.stream()
                    .map(tag -> converterToDTO.apply(tag))
                    .findFirst();
            return tagToFind;
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public int create(TagDTO tagDTO) throws ServiceException {
        PreparedValidatorChain<TagDTO> validatorChain = new IntermediateTagLink();
        validatorChain.linkWith(new TagNameValidatorLink());
        if (validatorChain.validate(tagDTO)) {
            try {
                int id = tagDao.create(converterToEntity.apply(tagDTO));
                return id;
            } catch (DaoException e) {
                throw new ServiceException("exception in dao", e.getCause());
            }
        }
        return 0;
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            tagDao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }
}
