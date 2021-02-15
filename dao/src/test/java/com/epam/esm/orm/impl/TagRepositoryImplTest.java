package com.epam.esm.orm.impl;

import com.epam.esm.config.ConfigDB;
import com.epam.esm.exception.DaoException;
import com.epam.esm.orm.TagRepository;
import com.epam.esm.persistence.HibernateGiftCertificateEntity;
import com.epam.esm.persistence.HibernateTagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {ConfigDB.class},
        loader = AnnotationConfigContextLoader.class)
class TagRepositoryImplTest {

    private static final String PREFIX_NAME = "tag ";

    @Autowired
    private TagRepository repository;

    @Test
    public void testCreateTag() throws DaoException {
        HibernateTagEntity tag = new HibernateTagEntity();
        tag.setName("name");
        HibernateGiftCertificateEntity cert = new HibernateGiftCertificateEntity();
        cert.setName("cert");
        cert.setDescription("description");
        cert.setDuration(10);
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(1000);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        tag.addCertificate(cert);
        int newTagId = repository.create(tag);
        assertEquals(101, newTagId);
    }

    // FIXME: 11.02.2021 no dependencies on certificates in first tag after retrieve
    @Test
    public void testCreateTwoTags() throws DaoException {
        HibernateTagEntity tag = new HibernateTagEntity();
        tag.setName("name");
        HibernateGiftCertificateEntity cert = new HibernateGiftCertificateEntity();
        cert.setName("cert");
        cert.setDescription("description");
        cert.setDuration(10);
        cert.setCreateDate(Date.valueOf(LocalDate.now()));
        cert.setPrice(1000);
        cert.setLastUpdateDate(Timestamp.valueOf(LocalDateTime.now()));
        tag.addCertificate(cert);
        repository.create(tag);
        HibernateTagEntity tagSec = new HibernateTagEntity();
        tagSec.setName("name1");
        tagSec.addCertificate(cert);
        repository.create(tagSec);
        HibernateTagEntity tagFirst = repository.find(101).get(0);
        HibernateTagEntity tagSecond = repository.find(102).get(0);
        assertEquals(1, tagFirst.getCertificateEntitySet().stream().findFirst().get().getId());
        assertEquals(1, tagSecond.getCertificateEntitySet().stream().findFirst().get().getId());
    }

    @Test
    public void testDeleteTag() throws DaoException {
        repository.delete(1);
        List<HibernateTagEntity> result = repository.find(1);
        assertNull(result.get(0));
    }

    @BeforeEach
    public void injectDefaultData() throws DaoException {
        for (int i = 0; i < 100; i++) {
            HibernateTagEntity tag = new HibernateTagEntity();
            tag.setName(PREFIX_NAME + i);
            repository.create(tag);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void testFindAllByPage(int page) throws DaoException {
        List<HibernateTagEntity> result = repository.findAll(10, page);
        assertNotNull(result);
        assertEquals((page - 1) * 10L + 1, result.get(0).getId());
        assertEquals(page * 10L, result.get(result.size() - 1).getId());
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5, 6, 7})
    public void testFindAllByLimit(int limit) throws DaoException {
        List<HibernateTagEntity> result = repository.findAll(limit, 1);
        assertNotNull(result);
        assertEquals(1, result.get(0).getId());
        assertEquals(limit, result.get(result.size() - 1).getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 101, 250, 380, 500})
    public void testFindSpecificTag(long id) throws DaoException {
        List<HibernateTagEntity> result = repository.find(id);
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
    }
}