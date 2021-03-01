package com.epam.esm.jdbc;

import com.epam.esm.Repository;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;

import java.util.List;

public interface GiftCertificateDao extends Repository<GiftCertificate> {

    int create(GiftCertificate entity) throws DaoException;

    void delete(long id) throws DaoException;

    void update(GiftCertificate certificateForUpdate, long id) throws DaoException;

    List<GiftCertificate> sortCertificates(String field, String method, int limit, int page) throws DaoException;

    List<GiftCertificate> searchByName(String name, int limit, int page) throws DaoException;

    List<GiftCertificate> searchByDescription(String description, int limit, int page) throws DaoException;

    List<GiftCertificate> searchByTag(String nameOfTag, int limit, int page) throws DaoException;

}
