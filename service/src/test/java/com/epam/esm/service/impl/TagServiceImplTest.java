package com.epam.esm.service.impl;

import com.epam.esm.TagDao;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.impl.TagDaoImpl;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
class TagServiceImplTest {

    @Autowired
    private TagService service;
    @Mock
    private TagDao dao = Mockito.mock(TagDaoImpl.class);

    @Test
    public void testCreate() throws DaoException, ServiceException {
        int expected = 7;
        Tag tag = new Tag(7, "name");
        Mockito.doReturn(expected).when(dao).create(tag);
        int actual = service.create(new TagDTO(7, "name"));
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 3, 6})
    public void testDelete(long id) throws DaoException {
        Mockito.doNothing().when(dao).delete(id);
    }
}