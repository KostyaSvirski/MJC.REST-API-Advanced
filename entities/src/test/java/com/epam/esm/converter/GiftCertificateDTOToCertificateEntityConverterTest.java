package com.epam.esm.converter;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.GiftCertificate;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateDTOToCertificateEntityConverterTest {

    private GiftCertificateDTOToEntityConverter converter =
            new GiftCertificateDTOToEntityConverter();

    private GiftCertificateDTO[] paramsToCheck = {
            new GiftCertificateDTO(1, "name", "descr", 100,
                    "P10Y2M3D", Instant.now().toString(), Instant.now().toString(),
                    Arrays.asList(new TagDTO[]{new TagDTO()})),
            new GiftCertificateDTO(1, "name", 100,
                    "P10Y2M3D", Instant.now().toString(), Instant.now().toString(),
                    Arrays.asList(new TagDTO[]{new TagDTO()}))
        };


    @Test
    public void testConverter() {
        for (GiftCertificateDTO certificateDTO : paramsToCheck) {
            GiftCertificate certificate = converter.apply(certificateDTO);
            assertTrue(true);
        }
    }
}