package com.epam.esm.hibernate;

import com.epam.esm.Repository;
import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CertificateRepository extends Repository<GiftCertificateEntity> {

    @Transactional
    int create(GiftCertificateEntity entity) throws DaoException;

    @Transactional
    void delete(long id) throws DaoException;

    @Transactional
    void update(GiftCertificateEntity certificateForUpdate) throws DaoException;

    List<GiftCertificateEntity> sortCertificatesByName(String method, int limit, int page)
            throws DaoException;
    List<GiftCertificateEntity> sortCertificatesByCreateDate(String method, int limit, int page)
            throws DaoException;

    List<GiftCertificateEntity> searchByName(String name, int limit, int page)
            throws DaoException;

    List<GiftCertificateEntity> searchByDescription(String description, int limit, int page)
            throws DaoException;

    List<GiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page)
            throws DaoException;
}
