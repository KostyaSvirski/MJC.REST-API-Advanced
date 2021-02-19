package com.epam.esm.service.impl;

import com.epam.esm.converter.TagDTOToTagEntityConverter;
import com.epam.esm.converter.TagEntityToTagDTOConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hibernate.TagRepository;
import com.epam.esm.persistence.TagEntity;
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

    private final TagEntityToTagDTOConverter converterToDTO;
    private final TagDTOToTagEntityConverter converterToEntity;
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagEntityToTagDTOConverter converterToDTO,
                          TagDTOToTagEntityConverter converterToEntity, TagRepository tagRepository) {
        this.converterToDTO = converterToDTO;
        this.converterToEntity = converterToEntity;
        this.tagRepository = tagRepository;
    }

    @Override
    public List<TagDTO> findAll(int limit, int page) throws ServiceException {
        try {
            List<TagEntity> listOfEntities = tagRepository.findAll(limit, page);
            return listOfEntities.stream()
                    .map(converterToDTO)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public Optional<TagDTO> find(long id) throws ServiceException {
        try {
            Optional<TagEntity> tagFromDao = tagRepository.find(id);
            return tagFromDao.map(converterToDTO);
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
                return tagRepository.create(converterToEntity.apply(tagDTO));
            } catch (DaoException e) {
                throw new ServiceException("exception in dao", e.getCause());
            }
        }
        return 0;
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            tagRepository.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }
}
