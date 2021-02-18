package com.epam.esm.service.impl;

import com.epam.esm.jdbc.GiftCertificateDao;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.GiftCertificateDTOToEntityConverter;
import com.epam.esm.converter.GiftCertificateEntityToDTOConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.jdbc.impl.CertificateDaoImpl;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
class GiftCertificateServiceImplTest {

    @Mock
    private final GiftCertificateDao dao = Mockito.mock(CertificateDaoImpl.class);
    @Mock
    private final GiftCertificateEntityToDTOConverter entityToDTOConverter =
            Mockito.mock(GiftCertificateEntityToDTOConverter.class);
    @Mock
    private final GiftCertificateDTOToEntityConverter dtoToEntityConverter =
            Mockito.mock(GiftCertificateDTOToEntityConverter.class);
    @InjectMocks
    private GiftCertificateServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        assertNotNull(service.findAll(1, 5));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5})
    public void testFindSpecificCert(long id) throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(id));
        Mockito.when(dao.find(id)).thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(id));
        assertEquals(service.find(id), Optional.of(new GiftCertificateDTO(id)));
    }

    @Test
    public void testFindNotExistingCert() throws DaoException, ServiceException {
        Mockito.when(dao.find(Mockito.anyLong()))
                .thenReturn(Collections.emptyList());
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(null);
        assertEquals(service.find(Mockito.anyLong()), Optional.empty());
    }

    @Test
    public void testCreate() throws DaoException, ServiceException {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100);
        certDTO.setCreateDate(Instant.now().toString());
        certDTO.setLastUpdateDate(Instant.now().toString());
        certDTO.setDuration("P0Y10M11D");
        certDTO.setTags(Collections.singletonList(new TagDTO(1, "name")));
        Mockito.when(dao.create(Mockito.any())).thenReturn(7);
        int actual = service.create(certDTO);
        assertEquals(actual, 7);
    }


    // FIXME: 05.02.2021 not throws
    @Test
    public void testUpdate() throws DaoException {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100);
        Mockito.doThrow(new DaoException()).when(dao).update(Mockito.any(), Mockito.eq(1));
        Mockito.when(dtoToEntityConverter.apply(certDTO)).thenReturn(new GiftCertificate());
        Throwable thrown = assertThrows(ServiceException.class, () -> service.update(certDTO, 1));
        assertNotNull(thrown);
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc"})
    public void testSortingMethod(String method) throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.sortCertificates(Mockito.any(), Mockito.eq(method), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.sortByField("name_of_certificate", method, 1, 1);
        assertEquals(Collections.singletonList(new GiftCertificateDTO(1)), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ask", "desk"})
    public void testSortingIncMethod(String method) throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.sortCertificates(Mockito.any(), Mockito.eq(method), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.sortByField("name_of_certificate", method, 1, 1);
        assertEquals(Collections.emptyList(), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name_of_certificate", "create_date"})
    public void testSortingField(String field) throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.sortCertificates(Mockito.eq(field), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.sortByField(field, "asc", 1, 1);
        assertEquals(Collections.singletonList(new GiftCertificateDTO(1)), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"description", "price"})
    public void testSortingIncField(String field) throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.sortCertificates(Mockito.eq(field), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.sortByField(field, "asc", 1, 1);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testExceptionSort() throws DaoException {
        Mockito.when(dao.sortCertificates(Mockito.any(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new DaoException());
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        Throwable throwable = assertThrows(ServiceException.class, () -> service.sortByField("create_date",
                "asc", 1, 1));
        String expected = "exception in dao";
        assertEquals(expected, throwable.getMessage());

    }

    @Test
    public void testFindByPartOfDescription() throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.searchByDescription(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.findByPartOfDescription
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO(1)), actual);
    }

    @Test
    public void testFindByPartOfName() throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.searchByName(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.findByPartOfName
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO(1)), actual);
    }

    @Test
    public void testFindByTagName() throws DaoException, ServiceException {
        List<GiftCertificate> resultList = new ArrayList<>();
        resultList.add(new GiftCertificate(1));
        Mockito.when(dao.searchByTag(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO(1));
        List<GiftCertificateDTO> actual = service.findByTag
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO(1)), actual);
    }

}