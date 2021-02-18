package com.epam.esm.service.impl;

import com.epam.esm.jdbc.GiftCertificateDao;
import com.epam.esm.converter.GiftCertificateDTOToEntityConverter;
import com.epam.esm.converter.GiftCertificateEntityToDTOConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateCertificateLink;
import com.epam.esm.validator.realisation.IntermediateSortLink;
import com.epam.esm.validator.realisation.certificate.CertificateNameValidatorLink;
import com.epam.esm.validator.realisation.certificate.DescriptionValidatorLink;
import com.epam.esm.validator.realisation.certificate.DurationValidatorLink;
import com.epam.esm.validator.realisation.certificate.PriceValidatorLink;
import com.epam.esm.validator.realisation.certificate.TagsValidatorLink;
import com.epam.esm.validator.realisation.sort.FieldValidatorLink;
import com.epam.esm.validator.realisation.sort.MethodValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateEntityToDTOConverter converterToDto;
    private GiftCertificateDTOToEntityConverter converterToEntity;
    private GiftCertificateDao dao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateEntityToDTOConverter converterToDto,
                                      GiftCertificateDTOToEntityConverter converterToEntity,
                                      GiftCertificateDao dao) {
        this.converterToDto = converterToDto;
        this.converterToEntity = converterToEntity;
        this.dao = dao;
    }

    public GiftCertificateServiceImpl() {
    }

    @Override
    public List<GiftCertificateDTO> findAll(int limit, int page) throws ServiceException {
        try {
            List<GiftCertificate> listFromDao = dao.findAll(limit, page);
            return createResultList(listFromDao);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public Optional<GiftCertificateDTO> find(long id) throws ServiceException {
        try {
            Optional<GiftCertificate> certificateFromDao = dao.find(id);
            return certificateFromDao.map(converterToDto);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }

    }

    @Override
    public int create(GiftCertificateDTO certificateDTO) throws ServiceException {
        PreparedValidatorChain<GiftCertificateDTO> chain = new IntermediateCertificateLink();
        chain.linkWith(new CertificateNameValidatorLink())
                .linkWith(new DescriptionValidatorLink())
                .linkWith(new DurationValidatorLink())
                .linkWith(new PriceValidatorLink())
                .linkWith(new TagsValidatorLink());
        if (chain.validate(certificateDTO)) {
            GiftCertificate certificate = converterToEntity.apply(certificateDTO);
            try {
                return dao.create(certificate);
            } catch (DaoException e) {
                throw new ServiceException("exception in dao", e.getCause());
            }
        }
        return 0;
    }

    @Override
    public boolean update(GiftCertificateDTO certificate, long id) throws ServiceException {
        PreparedValidatorChain<GiftCertificateDTO> chain = new CertificateNameValidatorLink();
        chain.linkWith(new DescriptionValidatorLink())
                .linkWith(new DurationValidatorLink())
                .linkWith(new PriceValidatorLink());
        if (chain.validate(certificate)) {
            GiftCertificate certificateForUpdate = converterToEntity.apply((certificate));
            try {
                dao.update(certificateForUpdate, id);
                return true;
            } catch (DaoException e) {
                throw new ServiceException("exception in dao", e.getCause());
            }
        }
        return false;
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            dao.delete(id);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public List<GiftCertificateDTO> findByPartOfName(String partOfName, int limit, int page) throws ServiceException {
        try {
            List<GiftCertificate> listFromDao = dao.searchByName(partOfName, limit, page);
            return createResultList(listFromDao);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public List<GiftCertificateDTO> findByPartOfDescription(String partOfDescription, int limit, int page)
            throws ServiceException {
        try {
            List<GiftCertificate> listFromDao = dao.searchByDescription(partOfDescription, limit, page);
            return createResultList(listFromDao);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public List<GiftCertificateDTO> findByTag(String nameOfTag, int limit, int page) throws ServiceException {
        try {
            List<GiftCertificate> listFromDao = dao.searchByTag(nameOfTag, limit, page);
            return createResultList(listFromDao);
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    @Override
    public List<GiftCertificateDTO> sortByField(String field, String method, int limit, int page)
            throws ServiceException {
        try {
            Map<String, String> tempMapForValidateParams = new HashMap<>();
            tempMapForValidateParams.put("method", method);
            tempMapForValidateParams.put("field", field);
            PreparedValidatorChain<Map<String, String>> chain = new IntermediateSortLink();
            chain.linkWith(new FieldValidatorLink()).linkWith(new MethodValidatorLink());
            if (chain.validate(tempMapForValidateParams)) {
                List<GiftCertificate> listFromDao = dao.sortCertificates(field, method, limit, page);
                return createResultList(listFromDao);
            }
            return new ArrayList<>();
        } catch (DaoException e) {
            throw new ServiceException("exception in dao", e.getCause());
        }
    }

    private List<GiftCertificateDTO> createResultList(List<GiftCertificate> listFromDao) {
        return listFromDao.stream()
                .map(certificate -> converterToDto.apply(certificate))
                .collect(Collectors.toList());
    }


}
