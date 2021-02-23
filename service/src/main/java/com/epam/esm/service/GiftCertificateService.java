package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDTO> {

    int create(GiftCertificateDTO bean);

    @Transactional
    void delete(long id) throws ServiceException;

    boolean update(GiftCertificateDTO certificate, long id) throws ServiceException;

    List<GiftCertificateDTO> findByPartOfName(String partOfName, int limit, int page);

    List<GiftCertificateDTO> findByPartOfDescription(String partOfDescription, int limit, int page);

    List<GiftCertificateDTO> findByTag(String idOfTag, int limit, int page);

    List<GiftCertificateDTO> sortByField(String field, String method, int limit, int page);
}
