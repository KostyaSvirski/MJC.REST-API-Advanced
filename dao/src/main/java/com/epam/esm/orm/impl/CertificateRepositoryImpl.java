package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.CertificateRepository;
import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String HQL_RETRIEVE_ALL = "from HibernateGiftCertificateEntity cert";
    private static final String HQL_ORDER_BY_ID = "order by cert.id";
    private static final String HQL_ORDER_BY_NAME = "order by cert.name";
    private static final String HQL_ORDER_BY_CREATE_DATE = "order by cert.create_date";
    private static final String HQL_CONDITION_DESCRIPTION = "where cert.description =: description";
    private static final String HQL_CONDITION_NAME = "where cert.name =: name";
    private static final String HQL_CONDITION_TAG = "where cert.name =: name";

    @Autowired
    private SessionFactory factory;

    @Override
    public List<HibernateGiftCertificateEntity> find(long id) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            HibernateGiftCertificateEntity cert = session.get(HibernateGiftCertificateEntity.class, id);
            transaction.commit();
            return Collections.singletonList(cert);
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateGiftCertificateEntity> findAll(int limit, int page) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List certs = session.createQuery(HQL_RETRIEVE_ALL + HQL_ORDER_BY_ID)
                    .setFirstResult(limit * (page - 1))
                    .setMaxResults(limit)
                    .getResultList();
            transaction.commit();
            return certs;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public int create(HibernateGiftCertificateEntity entity) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Integer idOfNewCert = (Integer) session.save(entity);
            transaction.commit();
            return idOfNewCert;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            HibernateGiftCertificateEntity certToDelete = session
                    .get(HibernateGiftCertificateEntity.class, id);
            session.delete(certToDelete);
            transaction.commit();
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void update(HibernateGiftCertificateEntity certificateForUpdate, long id) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            certificateForUpdate.setId(id);
            session.update(certificateForUpdate);
            transaction.commit();
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateGiftCertificateEntity> sortCertificatesByName(String method, int limit, int page)
            throws DaoException {
        return getSortedCertificates(method, limit, page, HQL_ORDER_BY_NAME);
    }

    @Override
    public List<HibernateGiftCertificateEntity> sortCertificatesByCreateDate
            (String method, int limit, int page)
            throws DaoException {
        return getSortedCertificates(method, limit, page, HQL_ORDER_BY_CREATE_DATE);

    }

    private List<HibernateGiftCertificateEntity> getSortedCertificates
            (String method, int limit, int page, String hqlOrderByName) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            List certs = session.createQuery(HQL_RETRIEVE_ALL + hqlOrderByName + method)
                    .setMaxResults(limit)
                    .setFirstResult(limit * (page - 1))
                    .getResultList();
            transaction.commit();
            return certs;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<HibernateGiftCertificateEntity> searchByName
            (String name, int limit, int page) throws DaoException {
        return searchCerts(limit, page, HQL_CONDITION_NAME, name);
    }

    @Override
    public List<HibernateGiftCertificateEntity> searchByDescription
            (String description, int limit, int page) throws DaoException {
        return searchCerts(limit, page, HQL_CONDITION_DESCRIPTION, description);
    }

    private List<HibernateGiftCertificateEntity> searchCerts
            (int limit, int page, String hqlConditionDescription, String param) throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
            Query query = session.createQuery
                    (HQL_RETRIEVE_ALL + hqlConditionDescription + HQL_ORDER_BY_ID);
            query.setParameter(1, param);
            List certs = query.setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
            transaction.commit();
            return certs;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }

    // TODO: 09.02.2021
    @Override
    public List<HibernateGiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page)
            throws DaoException {
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();
/*            List certs = session.createQuery("");*/
            transaction.commit();
            return null;
        } catch (Throwable e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DaoException(e.getMessage());
        }
    }
}
