package com.epam.esm.orm.impl;

import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.CertificateRepository;
import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import com.epam.esm.persistence.HibernateTagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String HQL_RETRIEVE_ALL = "from HibernateGiftCertificateEntity cert ";
    private static final String HQL_ORDER_BY_ID = "order by cert.id ";
    private static final String HQL_ORDER_BY_NAME = "order by cert.name ";
    private static final String HQL_ORDER_BY_CREATE_DATE = "order by cert.create_date ";
    private static final String HQL_CONDITION_DESCRIPTION = "where cert.description =: description ";
    private static final String HQL_CONDITION_NAME = "where cert.name =: name ";

    @Qualifier("test")
    @Autowired
    private EntityManagerFactory factory;

    @Override
    public List<HibernateGiftCertificateEntity> find(long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            HibernateGiftCertificateEntity cert = em.find(HibernateGiftCertificateEntity.class, id);
            /*em.detach(cert);*/
            return Collections.singletonList(cert);
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<HibernateGiftCertificateEntity> findAll(int limit, int page) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            return em.createQuery(HQL_RETRIEVE_ALL + HQL_ORDER_BY_ID)
                    .setFirstResult((page - 1) * limit)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public int create(HibernateGiftCertificateEntity entity) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            int idNewCert = (int) entity.getId();
            em.getTransaction().commit();
            return idNewCert;
        } catch (Throwable e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void delete(long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            em.getTransaction().begin();
            HibernateGiftCertificateEntity certToDelete = em.find(HibernateGiftCertificateEntity.class, id);
            em.remove(certToDelete);
        } catch (Throwable e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public void update(HibernateGiftCertificateEntity certificateExampleForUpdate, long id) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            Optional<HibernateGiftCertificateEntity> certWrapper = find(id).stream().findAny();
            if (certWrapper.isPresent()) {
                HibernateGiftCertificateEntity cert = certWrapper.get();
                cert = insertDataForUpdate(certificateExampleForUpdate, cert);
                em.getTransaction().begin();
                em.merge(cert);
                em.getTransaction().commit();
            }
        } catch (Throwable e) {
            if (em != null) {
                em.getTransaction().rollback();
            }
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private HibernateGiftCertificateEntity insertDataForUpdate
            (HibernateGiftCertificateEntity example, HibernateGiftCertificateEntity cert) {
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
        return cert;
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
            (String method, int limit, int page, String hqlOrderBy) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            return em.createQuery(HQL_RETRIEVE_ALL + hqlOrderBy + method)
                    .setFirstResult(limit * (page - 1))
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
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
            (int limit, int page, String hqlCondition, String param) throws DaoException {
        EntityManager em = null;
        try {
            em = factory.createEntityManager();
            Query query = em.createQuery(HQL_RETRIEVE_ALL + hqlCondition);
            query.setParameter(1, param);
            return query.setFirstResult(limit * (page - 1))
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Throwable e) {
            throw new DaoException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // TODO: 09.02.2021
    @Override
    public List<HibernateGiftCertificateEntity> searchByTag(String nameOfTag, int limit, int page)
            throws DaoException {
        return null;
    }
}
