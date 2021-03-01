package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CertificateNameValidatorLinkTest {

    private CertificateNameValidatorLink validator = new CertificateNameValidatorLink();
    private String[] paramsToCheck = {"asdf", "as asdsad s", "-a-dc1c", "basbc - AAAAA", "CSAC"};
    private String[] incParamsToCheck = {"asdfghjklpoiuytrewqasdfgvcxzxcvbnvjgyftrertyjhgfdsdfgchvfds"};

    @Test
    public void testValidation() {
        for(String param : paramsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setName(param);
            assertTrue(validator.validate(certificate));
        }
    }

    @Test
    public void testIncParams() {
        for(String param : incParamsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setName(param);
            assertFalse(validator.validate(certificate));
        }
    }

}