package com.epam.esm.service.impl;

import com.epam.esm.converter.GiftCertificateDTOToEntityConverter;
import com.epam.esm.converter.GiftCertificateEntityToDTOConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import com.epam.esm.persistence.TagEntity;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateCertificateLink;
import com.epam.esm.validator.realisation.IntermediateSortLink;
import com.epam.esm.validator.realisation.certificate.*;
import com.epam.esm.validator.realisation.sort.FieldValidatorLink;
import com.epam.esm.validator.realisation.sort.MethodValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateEntityToDTOConverter converterToDto;
    private final GiftCertificateDTOToEntityConverter converterToEntity;
    private final CertificateRepository repository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateEntityToDTOConverter converterToDto,
                                      GiftCertificateDTOToEntityConverter converterToEntity,
                                      CertificateRepository repository) {
        this.converterToDto = converterToDto;
        this.converterToEntity = converterToEntity;
        this.repository = repository;
    }

    @Override
    public List<GiftCertificateDTO> findAll(int limit, int page) {
        return createResultList(repository.findAll(limit, page));
    }

    @Override
    public Optional<GiftCertificateDTO> find(long id) {
        Optional<GiftCertificateEntity> certificateFromDao = repository.find(id);
        return certificateFromDao.map(converterToDto);
    }

    @Override
    public int create(GiftCertificateDTO certificateDTO) {
        PreparedValidatorChain<GiftCertificateDTO> chain = new IntermediateCertificateLink();
        chain.linkWith(new CertificateNameValidatorLink())
                .linkWith(new DescriptionValidatorLink())
                .linkWith(new DurationValidatorLink())
                .linkWith(new PriceValidatorLink())
                .linkWith(new TagsValidatorLink());
        if (chain.validate(certificateDTO)) {
            GiftCertificateEntity certificate = converterToEntity.apply(certificateDTO);
            return repository.create(certificate);
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
            GiftCertificateEntity certificateForUpdate = converterToEntity.apply((certificate));
            Optional<GiftCertificateEntity> certFromDao = repository.find(id);
            if (certFromDao.isPresent()) {
                insertDataForUpdate(certificateForUpdate, certFromDao.get());
                repository.update(certificateForUpdate);
                return true;
            } else {
                throw new ServiceException("not found");
            }
        }
        return false;
    }

    private void insertDataForUpdate
            (GiftCertificateEntity example, GiftCertificateEntity cert) {
        if (example.getName() != null) {
            cert.setName(example.getName());
        }
        if (example.getDescription() != null) {
            cert.setDescription(example.getDescription());
        }
        if (example.getLastUpdateDate() != null) {
            cert.setLastUpdateDate(example.getLastUpdateDate());
        }
        if (example.getCreateDate() != null) {
            cert.setCreateDate(example.getCreateDate());
        }
        if (example.getDuration() != 0) {
            cert.setDuration(example.getDuration());
        }
        if (example.getPrice() != 0) {
            cert.setPrice(example.getPrice());
        }
        if (example.getTagsDependsOnCertificate() != null) {
            cert.setTagsDependsOnCertificate(example.getTagsDependsOnCertificate());
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        Optional<GiftCertificateEntity> certWrapper = repository.find(id);
        if (certWrapper.isPresent()) {
            GiftCertificateEntity certToDelete = certWrapper.get();
            Set<TagEntity> tagEntities = certToDelete.getTagsDependsOnCertificate();
            tagEntities.forEach(tagEntity -> tagEntity.removeCertificate(certToDelete));
            certToDelete.setTagsDependsOnCertificate(tagEntities);
            repository.delete(certToDelete);
        } else {
            throw new ServiceException("not found");
        }
    }

    @Override
    public List<GiftCertificateDTO> findByPartOfName(String partOfName, int limit, int page) {
        return createResultList(repository.searchByName(partOfName, limit, page));
    }

    @Override
    public List<GiftCertificateDTO> findByPartOfDescription(String partOfDescription, int limit, int page) {
        return createResultList(repository.searchByDescription(partOfDescription, limit, page));
    }

    @Override
    public List<GiftCertificateDTO> findByTag(String nameOfTag, int limit, int page) {
        return createResultList(repository.searchByTag(nameOfTag, limit, page));
    }

    @Override
    public List<GiftCertificateDTO> sortByField(String field, String method, int limit, int page) {
        Map<String, String> tempMapForValidateParams = new HashMap<>();
        tempMapForValidateParams.put("method", method);
        tempMapForValidateParams.put("field", field);
        PreparedValidatorChain<Map<String, String>> chain = new IntermediateSortLink();
        chain.linkWith(new FieldValidatorLink()).linkWith(new MethodValidatorLink());
        if (chain.validate(tempMapForValidateParams)) {
            List<GiftCertificateEntity> listFromDao;
            if (field.equals("name_of_certificate")) {
                listFromDao = repository.sortCertificatesByName(method, limit, page);
            } else {
                listFromDao = repository.sortCertificatesByCreateDate
                        (method, limit, page);
            }
            return createResultList(listFromDao);
        }
        return new ArrayList<>();
    }

    private List<GiftCertificateDTO> createResultList(List<GiftCertificateEntity> listFromDao) {
        return listFromDao.stream()
                .map(converterToDto)
                .collect(Collectors.toList());
    }
}
