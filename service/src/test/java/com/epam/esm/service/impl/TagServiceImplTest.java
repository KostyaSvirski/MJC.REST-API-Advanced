package com.epam.esm.service.impl;

import com.epam.esm.jdbc.TagDao;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.TagDTOToTagEntityConverter;
import com.epam.esm.converter.TagEntityToTagDTOConverter;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.jdbc.impl.TagDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
class TagServiceImplTest {

    @Mock
    private final TagDao dao = Mockito.mock(TagDaoImpl.class);
    @Mock
    private final TagDTOToTagEntityConverter converterToEntity = Mockito.mock(TagDTOToTagEntityConverter.class);
    @Mock
    private final TagEntityToTagDTOConverter converterToDTO = Mockito.mock(TagEntityToTagDTOConverter.class);
    @InjectMocks
    private TagServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testCreate(long id) throws DaoException, ServiceException {
        TagDTO tag = new TagDTO(id, "gg");
        Mockito.when(converterToEntity.apply(tag)).thenReturn(new Tag(id));
        Mockito.when(dao.create(Mockito.any())).thenReturn((int) id);
        int actual = service.create(tag);
        assertEquals(id, actual);
    }

    @Test
    public void testFindAll() throws DaoException, ServiceException {
        Mockito.when(dao.findAll(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(new Tag(1)));
        Mockito.when(converterToDTO.apply(Mockito.any())).thenReturn(new TagDTO());
        List<TagDTO> actual = service.findAll(Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new TagDTO()), actual);
    }

    @Test
    public void testFindAllException() throws DaoException {
        Mockito.when(dao.findAll(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new DaoException());
        Throwable throwable = assertThrows(ServiceException.class,
                () -> service.findAll(Mockito.anyInt(), Mockito.anyInt()));
        String expected = "exception in dao";
        assertEquals(expected, throwable.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindSpecificTag(long id) throws DaoException, ServiceException {
        Mockito.when(dao.find(Mockito.eq(id)))
                .thenReturn(Collections.singletonList(new Tag(id)));
        Mockito.when(converterToDTO.apply(Mockito.any())).thenReturn(new TagDTO(id));
        Optional<TagDTO> actual = service.find(id);
        assertEquals(Optional.of(new TagDTO(id)), actual);
    }

    @Test
    public void testFindSpecificTagNotFound() throws DaoException, ServiceException {
        Mockito.when(dao.find(Mockito.anyLong()))
                .thenReturn(Collections.emptyList());
        Optional<TagDTO> actual = service.find(Mockito.anyLong());
        assertEquals(Optional.empty(), actual);
    }


    @ParameterizedTest
    @ValueSource(longs = {1, 3, 6})
    public void testDelete(long id) throws DaoException {
        Mockito.doNothing().when(dao).delete(id);
    }
}