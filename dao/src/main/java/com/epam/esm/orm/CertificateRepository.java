package com.epam.esm.orm;

import com.epam.esm.Repository;
import com.epam.esm.exception.DaoException;
import com.epam.esm.persistence.HibernateGiftCertificateEntity;

import java.util.List;

public interface CertificateRepository extends Repository<HibernateGiftCertificateEntity> {

    int create(HibernateGiftCertificateEntity entity) throws DaoException;

    void delete(long id) throws DaoException;

    void update(HibernateGiftCertificateEntity certificateForUpdate, long id) throws DaoException;

    List<HibernateGiftCertificateEntity> sortCertificatesByName(String method, int limit, int page)
            throws DaoException;
    List<HibernateGiftCertificateEntity> sortCertificatesByCreateDate(String method, int limit, int page)
            throws DaoException;

    List<HibernateGiftCertificateEntity> searchByName(String name, int limit, int page)
            throws DaoException;

    List<HibernateGiftCertificateEntity> searchByDescription(String description, int limit, int page)
            throws DaoException;

    List<HibernateGiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page)
            throws DaoException;
}
