package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TagsValidatorLinkTest {

    private TagsValidatorLink validator = new TagsValidatorLink();
    private TagDTO[][] paramsToCheck = {{new TagDTO(1L, "first-name")},
            {new TagDTO(1L, "first-name"), new TagDTO(7L, "first"),
                    new TagDTO(10L, "asdf")}};
    private TagDTO[][] incParamsToCheck = {{new TagDTO(-1L, "first-name")},
            {new TagDTO(1L, null)}};

    @Test
    public void testValidation() {
        for (TagDTO[] param : paramsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            Set<TagDTO> tags = new HashSet<>(Arrays.asList(param));
            certificate.setTags(tags);
            assertTrue(validator.validate(certificate));
        }
    }

    @Test
    public void testValidationIncParams() {
        for (TagDTO[] param : incParamsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            Set<TagDTO> tags = new HashSet<>(Arrays.asList(param));
            certificate.setTags(tags);
            assertFalse(validator.validate(certificate));
        }
    }

}