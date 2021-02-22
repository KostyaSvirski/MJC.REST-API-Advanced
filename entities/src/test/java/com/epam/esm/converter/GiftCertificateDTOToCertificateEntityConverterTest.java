package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.persistence.GiftCertificateEntity;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateDTOToCertificateEntityConverterTest {

    private GiftCertificateDTOToEntityConverter converter =
            new GiftCertificateDTOToEntityConverter();

    private GiftCertificateDTO[] paramsToCheck = {
            new GiftCertificateDTO(1, "name", "descr", 100,
                    1, LocalDate.now().toString(), LocalDateTime.now().toString(),
                    new HashSet<>(Collections.singletonList(new TagDTO()))),
            new GiftCertificateDTO(1, "name", "descr", 100,
                    1, LocalDate.now().toString(), LocalDateTime.now().toString(),
                    new HashSet<>(Collections.singletonList(new TagDTO())))
        };


    @Test
    public void testConverter() {
        for (GiftCertificateDTO certificateDTO : paramsToCheck) {
            GiftCertificateEntity certificate = converter.apply(certificateDTO);
            assertTrue(certificate!= null);
        }
    }
}