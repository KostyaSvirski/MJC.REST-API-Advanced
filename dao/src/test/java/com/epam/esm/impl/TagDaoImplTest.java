package com.epam.esm.impl;

import com.epam.esm.TagDao;
import com.epam.esm.config.ConfigDB;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = ConfigDB.class)
@ExtendWith(SpringExtension.class)
class TagDaoImplTest {

    @Autowired
    private TagDao tagDao;

   /* @Test
    void testFindAll() throws DaoException {
        List<Tag> actual = tagDao.findAll();
        List<Tag> expected = new ArrayList<>();
        expected.add(new Tag(1, "first-tag"));
        expected.add(new Tag(3, "third-tag"));
        expected.add(new Tag(6, "new tag"));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 3, 6})
    void testFindSpecific(long id) throws DaoException {
        System.out.println("");
        assertEquals(id, tagDao.find(id).get(0).getId());
    }*/


}