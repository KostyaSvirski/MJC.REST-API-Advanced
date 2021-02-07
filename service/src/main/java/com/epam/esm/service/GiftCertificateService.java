package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService extends BaseService<GiftCertificateDTO> {

    int create(GiftCertificateDTO bean) throws ServiceException;

    void delete(long id) throws ServiceException;

    boolean update(GiftCertificateDTO certificate, long id) throws ServiceException;

    List<GiftCertificateDTO> findByPartOfName(String partOfName, int limit, int page) throws ServiceException;

    List<GiftCertificateDTO> findByPartOfDescription(String partOfDescription, int limit, int page)
            throws ServiceException;

    List<GiftCertificateDTO> findByTag(String idOfTag, int limit, int page) throws ServiceException;

    List<GiftCertificateDTO> sortByField(String field, String method, int limit, int page)
            throws ServiceException;
}
