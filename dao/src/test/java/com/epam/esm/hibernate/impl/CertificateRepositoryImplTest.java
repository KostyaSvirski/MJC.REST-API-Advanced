package com.epam.esm.hibernate.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.persistence.GiftCertificateEntity;
import com.epam.esm.persistence.TagEntity;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class CertificateRepositoryImplTest {

    @Autowired
    private CertificateRepository repository;

    @BeforeEach
    public void defaultData() throws DaoException {
        for (int i = 0; i < 100; i++) {
            GiftCertificateEntity cert = new GiftCertificateEntity();
            cert.setName("name " + i);
            cert.setDescription("descr " + i);
            cert.setCreateDate(Date.valueOf(LocalDate.now()));
            cert.setPrice(100);
            cert.setDuration(10);
            cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
            TagEntity tag = new TagEntity();
            tag.setName("tag " + i);
            cert.addTag(tag);
            repository.create(cert);
        }
    }

    @Test
    public void testCreate() throws DaoException {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        int result = repository.create(cert);
        assertEquals(101, result);
    }

    @Test
    public void testCreateCertWithSomeTags() throws DaoException {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        TagEntity tagSec = new TagEntity();
        tagSec.setName("tag sec");
        cert.addTag(tagSec);
        int result = repository.create(cert);
        assertEquals(101, result);
        GiftCertificateEntity certFromRepo = repository.find(result).get();
        assertEquals(2, certFromRepo.getTagsDependsOnCertificate().size());
    }

    @Test
    public void testUpdateCert() throws DaoException {
        GiftCertificateEntity cert = new GiftCertificateEntity();
        cert.setName("name");
        cert.setDescription("descr");
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(100);
        cert.setDuration(10);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        TagEntity tag = new TagEntity();
        tag.setName("tag");
        cert.addTag(tag);
        int result = repository.create(cert);
        cert = repository.find(result).get();
        assertEquals(101, result);
        cert.setName("new name");
        TagEntity newTag = new TagEntity();
        newTag.setName("tag new ");
        cert.removeTag(tag);
        cert.addTag(newTag);
        repository.update(cert, result);
        GiftCertificateEntity certFromRepo = repository.find(101).get();
        assertEquals(certFromRepo.getId(), result);
        assertEquals(certFromRepo.getName(), "new name");
    }

    @Test
    public void testFindSpecificCert() throws DaoException {
        Optional<GiftCertificateEntity> cert = repository.find(1);
        assertTrue(cert.isPresent());
        Set<TagEntity> tagsDependsOnCert = cert.get().getTagsDependsOnCertificate();
        assertNotNull(cert);
        assertNotNull(tagsDependsOnCert);
    }

    @Test
    void testFindAllCerts() throws DaoException {
        List<GiftCertificateEntity> list = repository.findAll(10, 1);
        assertEquals(list.size(), 10);
        assertEquals(1, list.get(0).getId());
        assertEquals(10, list.get(list.size()-1).getId());
    }

    @Test
    public void testDeleteCert() throws DaoException {
        repository.delete(1);
        Optional<GiftCertificateEntity> cert = repository.find(1);
        assertFalse(cert.isPresent());
    }

    @Test
    public void testRetrieveSortedByNameCertsAsc() throws DaoException {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByName("asc", 10, 1);
        assertEquals(10, entities.size());
        assertEquals("name 0", entities.get(0).getName());
    }

    @Test
    public void testRetrieveSortedByNameCertsDesc() throws DaoException {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByName("desc", 10, 1);
        assertEquals(10, entities.size());
        assertEquals("name 99", entities.get(0).getName());
        assertEquals("name 90", entities.get(entities.size()-1).getName());
    }

    @Test
    public void testRetrieveSortedByNameCertsException() throws DaoException {
        assertThrows(DaoException.class, () -> repository
                .sortCertificatesByName("awef", 10, 1));
    }

    @Test
    public void testRetrieveSortedByDateCertsAsc() throws DaoException {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByCreateDate("asc", 10, 1);
        assertEquals(10, entities.size());
        assertEquals(-1, entities.get(0).getCreateDate()
                .compareTo(entities.get(entities.size() - 1).getCreateDate()));
    }

    @Test
    public void testRetrieveSortedByDateCertsDesc() throws DaoException {
        List<GiftCertificateEntity> entities = repository
                .sortCertificatesByCreateDate("desc", 10, 1);
        assertEquals(10, entities.size());
        assertEquals(1, entities.get(0).getCreateDate()
                .compareTo(entities.get(entities.size() - 1).getCreateDate()));
    }

    @Test
    public void testRetrieveSortedByDateCertsException() throws DaoException {
        assertThrows(DaoException.class, () -> repository
                .sortCertificatesByCreateDate("awef", 10, 1));
    }
}