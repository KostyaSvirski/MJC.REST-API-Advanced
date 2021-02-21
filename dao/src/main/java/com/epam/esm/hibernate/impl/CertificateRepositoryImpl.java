package com.epam.esm.hibernate.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String HQL_RETRIEVE_ALL = "from GiftCertificateEntity cert ";
    private static final String HQL_ORDER_BY_ID = "order by cert.id ";
    private static final String HQL_ORDER_BY_NAME = "order by cert.name ";
    private static final String HQL_ORDER_BY_CREATE_DATE = "order by cert.createDate ";
    private static final String HQL_CONDITION_DESCRIPTION = "where cert.description =: description ";
    private static final String HQL_CONDITION_NAME = "where cert.name =: name ";

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<GiftCertificateEntity> find(long id) {
        return Optional.ofNullable(em.find(GiftCertificateEntity.class, id));
    }

    @Override
    public List<GiftCertificateEntity> findAll(int limit, int page) {
        return em.createQuery(HQL_RETRIEVE_ALL + HQL_ORDER_BY_ID)
                .setFirstResult((page - 1) * limit)
                .setMaxResults(limit)
                .getResultList();
    }


    @Override
    public int create(GiftCertificateEntity entity) {
        em.persist(entity);
        return (int) entity.getId();
    }


    @Override
    public void delete(long id) {
        GiftCertificateEntity certToDelete = em.find(GiftCertificateEntity.class, id);
        em.remove(certToDelete);
    }

    @Override
    public void update(GiftCertificateEntity certificateExampleForUpdate) {
        em.merge(certificateExampleForUpdate);
    }

    @Override
    public List<GiftCertificateEntity> sortCertificatesByName(String method, int limit, int page) {
        return getSortedCertificates(method, limit, page, HQL_ORDER_BY_NAME);
    }

    @Override
    public List<GiftCertificateEntity> sortCertificatesByCreateDate(String method, int limit, int page) {
        return getSortedCertificates(method, limit, page, HQL_ORDER_BY_CREATE_DATE);
    }

    private List<GiftCertificateEntity> getSortedCertificates(String method, int limit, int page, String hqlOrderBy) {
        return em.createQuery(HQL_RETRIEVE_ALL + hqlOrderBy + method)
                .setFirstResult(limit * (page - 1))
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<GiftCertificateEntity> searchByName(String name, int limit, int page) {
        return searchCerts(limit, page, HQL_CONDITION_NAME, name);
    }

    @Override
    public List<GiftCertificateEntity> searchByDescription(String description, int limit, int page) {
        return searchCerts(limit, page, HQL_CONDITION_DESCRIPTION, description);
    }

    private List<GiftCertificateEntity> searchCerts(int limit, int page, String hqlCondition, String param) {
        Query query = em.createQuery(HQL_RETRIEVE_ALL + hqlCondition);
        query.setParameter(1, param);
        return query.setFirstResult(limit * (page - 1))
                .setMaxResults(limit)
                .getResultList();

    }

    // TODO: 09.02.2021
    @Override
    public List<GiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page) {
        return null;
    }
}
