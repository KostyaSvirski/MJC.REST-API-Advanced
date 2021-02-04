package com.epam.esm.service.impl;

import com.epam.esm.GiftCertificateDao;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.impl.CertificateDaoImpl;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
class GiftCertificateServiceImplTest {

    @Autowired
    private GiftCertificateService service;
    @Mock
    GiftCertificateDao dao = Mockito.mock(CertificateDaoImpl.class);

    @Test
    public void testCreate() throws DaoException, ServiceException {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100);
        certDTO.setCreateDate(Instant.now().toString());
        certDTO.setLastUpdateDate(Instant.now().toString());
        certDTO.setDuration("P0Y10M11D");
        certDTO.setTags(Arrays.asList(new TagDTO[] {new TagDTO(1, "name")}));
        Mockito.when(dao.create(Mockito.any())).thenReturn(1);
        int actual = service.create(certDTO);
        assertEquals(actual, 1);
    }

    @Test
    public void testUpdate() throws DaoException, ServiceException {
        GiftCertificateDTO certDTO = new GiftCertificateDTO();
        certDTO.setName("aaaa");
        certDTO.setPrice(100);
        certDTO.setLastUpdateDate(Instant.now().toString());
        Mockito.doNothing().when(dao).update(Mockito.any(), Mockito.anyLong());
        service.update(certDTO, Mockito.anyLong());

    }


}