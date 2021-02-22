package com.epam.esm.hibernate;

import com.epam.esm.Repository;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CertificateRepository extends Repository<GiftCertificateEntity> {

    @Transactional
    int create(GiftCertificateEntity entity);

    @Transactional
    void delete(long id);

    @Transactional
    void update(GiftCertificateEntity certificateForUpdate);

    List<GiftCertificateEntity> sortCertificatesByName(String method, int limit, int page);

    List<GiftCertificateEntity> sortCertificatesByCreateDate(String method, int limit, int page);

    List<GiftCertificateEntity> searchByName(String name, int limit, int page);

    List<GiftCertificateEntity> searchByDescription(String description, int limit, int page);

    List<GiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page);
}
