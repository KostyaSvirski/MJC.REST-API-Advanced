package com.epam.esm.service.impl;

import com.epam.esm.hibernate.CertificateRepository;
import com.epam.esm.hibernate.impl.CertificateRepositoryImpl;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.GiftCertificateDTOToEntityConverter;
import com.epam.esm.converter.GiftCertificateEntityToDTOConverter;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GiftCertificateServiceImplTest {

    @Mock
    private final CertificateRepository repository = Mockito.mock(CertificateRepositoryImpl.class);
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
    public void testFindAll() {
        GiftCertificateEntity entity = new GiftCertificateEntity();
        entity.setId(1);
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(entity);
        Mockito.when(repository.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(resultList);
        GiftCertificateDTO dto = new GiftCertificateDTO();
        dto.setId(1);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(dto);
        assertNotNull(service.findAll(1, 1));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4, 5})
    public void testFindSpecificCert(long id) {
        GiftCertificateEntity entity = new GiftCertificateEntity();
        entity.setId(id);
        Optional<GiftCertificateEntity> certWrapper = Optional.of(entity);
        Mockito.when(repository.find(id)).thenReturn(certWrapper);
        GiftCertificateDTO dto = new GiftCertificateDTO();
        dto.setId(id);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(dto);
        assertEquals(service.find(id), Optional.of(dto));
    }

    @Test
    public void testFindNotExistingCert() {
        Mockito.when(repository.find(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(null);
        assertEquals(service.find(Mockito.anyLong()), Optional.empty());
    }

    @Test
    public void testCreate() {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100L);
        certDTO.setCreateDate(Instant.now().toString());
        certDTO.setLastUpdateDate(Instant.now().toString());
        certDTO.setDuration(1);
        TagDTO dto = TagDTO.builder().id(1L).name("aaa").build();
        Set<TagDTO> setTag = new HashSet<>();
        setTag.add(dto);
        certDTO.setTags(setTag);
        Mockito.when(repository.create(dtoToEntityConverter.apply(certDTO))).thenReturn(1);
        int actual = service.create(certDTO);
        assertEquals(1, actual);
    }

    @Test
    public void testUpdate() {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100);
        Mockito.doThrow(new RuntimeException()).when(repository).update(Mockito.any());
        assertThrows(ServiceException.class, () -> service.update(certDTO, 1));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc"})
    public void testSortingMethod(String method) throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByName(Mockito.eq(method), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField("name_of_certificate", method, 1, 1);
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ask", "desk"})
    public void testSortingIncMethod(String method) throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByName(Mockito.eq(method), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField("name_of_certificate", method, 1, 1);
        assertEquals(Collections.emptyList(), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"name_of_certificate", "create_date"})
    public void testSortingField(String field) throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByName(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField(field, "asc", 1, 1);
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @ParameterizedTest
    @ValueSource(strings = {"description", "price"})
    public void testSortingIncField(String field) throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.sortCertificatesByCreateDate(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.sortByField(field, "asc", 1, 1);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testExceptionSort() throws DaoException {
        Mockito.when(repository.sortCertificatesByCreateDate(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new RuntimeException());
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        assertThrows(RuntimeException.class, () -> service.sortByField("create_date",
                "asc", 1, 1));

    }

    @Test
    public void testFindByPartOfDescription() throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.searchByDescription(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.findByPartOfDescription
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindByPartOfName() throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.searchByName(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.findByPartOfName
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindByTagName() throws DaoException, ServiceException {
        List<GiftCertificateEntity> resultList = new ArrayList<>();
        resultList.add(new GiftCertificateEntity().builder().id(1).build());
        Mockito.when(repository.searchByTag(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(resultList);
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new GiftCertificateDTO().builder().id(1)
                .build());
        List<GiftCertificateDTO> actual = service.findByTag
                (Mockito.any(), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new GiftCertificateDTO().builder().id(1).build()), actual);
    }

}