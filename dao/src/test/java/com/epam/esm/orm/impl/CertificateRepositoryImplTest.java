package com.epam.esm.orm.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.CertificateRepository;
import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import com.epam.esm.persistence.HibernateTagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository repository;

    @Test
    public void testCreate() throws DaoException {
        HibernateGiftCertificateEntity cert = new HibernateGiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        HibernateTagEntity tag = new HibernateTagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        int result = repository.create(cert);
        assertEquals(101, result);
    }

    // FIXME: 11.02.2021 exception collection was evicted
    @Test
    public void testCreateTwoCertsWithSimilarTags() throws DaoException {
        HibernateGiftCertificateEntity cert = new HibernateGiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        HibernateTagEntity tag = new HibernateTagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        int result = repository.create(cert);

        HibernateGiftCertificateEntity certSec = new HibernateGiftCertificateEntity();
        certSec.setName("name1");
        certSec.setDescription("descr1");
        certSec.setCreateDate(Date.valueOf(LocalDate.now()));
        certSec.setPrice(200);
        certSec.setDuration(10);
        certSec.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        HibernateTagEntity tag2 = new HibernateTagEntity();
        tag.setName("tag");
        certSec.addTag(tag2);
        int resultSec = repository.create(certSec);

        HibernateGiftCertificateEntity certFromRepo = repository.find(102).get(0);
        assertEquals(101, result);
        assertEquals(102, resultSec);
        assertEquals(101, certFromRepo.getTagsDependsOnCertificate().stream().findAny().get().getId());
    }

    // FIXME: 11.02.2021 works with lazy but not works with eager
    @Test
    public void testUpdateCert() throws DaoException {
        HibernateGiftCertificateEntity cert = new HibernateGiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        HibernateTagEntity tag = new HibernateTagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        int result = repository.create(cert);
        cert = repository.find(result).get(0);
        assertEquals(101, result);
        cert.setName("new name");
        HibernateTagEntity newTag = new HibernateTagEntity();
        newTag.setName("tag new ");
        cert.removeTag(tag);
        cert.addTag(newTag);
        repository.update(cert, result);
        HibernateGiftCertificateEntity certFromRepo = repository.find(101).get(0);
        assertEquals(certFromRepo.getId(), result);
        assertEquals(certFromRepo.getName(), "new name");
    }

    @BeforeEach
    public void defaultData() throws DaoException {
        for (int i = 0; i < 100; i++) {
            HibernateGiftCertificateEntity cert = new HibernateGiftCertificateEntity();
            cert.setName("name " + i);
            cert.setDescription("descr " + i);
            cert.setCreateDate(Date.valueOf(LocalDate.now()));
            cert.setPrice(100);
            cert.setDuration(10);
            cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            HibernateTagEntity tag = new HibernateTagEntity();
            tag.setName("tag " + i);
            cert.addTag(tag);
            repository.create(cert);
        }
    }

    @Test
    public void testFindSpecificCert() throws DaoException {
        HibernateGiftCertificateEntity cert = repository.find(1).get(0);
        Set<HibernateTagEntity> tagsDependsOnCert = cert.getTagsDependsOnCertificate();
        assertNotNull(cert);
        assertNotNull(cert.getTagsDependsOnCertificate());
    }

    @Test
    void testFindAllCerts() throws DaoException {
        List<HibernateGiftCertificateEntity> list = repository.findAll(10, 1);
        assertEquals(list.size(), 10);
    }

    // FIXME: 11.02.2021 exception collection was evicted
    @Test
    public void testDeleteCert() throws DaoException {
        repository.delete(1);
        List<HibernateGiftCertificateEntity> cert = repository.find(1);
        assertNull(cert.get(0).getName());
    }

    // FIXME: 12.02.2021 stackoverflow
    @Test
    public void testRetrieveSortedByNameCertsAsc() throws DaoException {
        List<HibernateGiftCertificateEntity> entities = repository
                .sortCertificatesByName("asc", 10, 1);
        assertEquals(entities.size(), 10);
        assertEquals(entities.get(0).getName(), "tag 0");
    }
}