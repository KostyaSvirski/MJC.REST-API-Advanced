package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceValidatorLinkTest {

    private PriceValidatorLink validator = new PriceValidatorLink();
    private long[] paramsToCheck = {1, 100, 15, 20,7};
    private long[] incParamsToCheck = {-5};

    @Test
    public void testValidation() {
        for(long param : paramsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setPrice(param);
            assertTrue(validator.validate(certificate));
        }
    }

    @Test
    public void testValidationIncParams() {
        for(long param : incParamsToCheck) {
            GiftCertificateDTO certificate = new GiftCertificateDTO();
            certificate.setPrice(param);
            assertFalse(validator.validate(certificate));
        }
    }

}