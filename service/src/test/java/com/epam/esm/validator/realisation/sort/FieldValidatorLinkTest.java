package com.epam.esm.validator.realisation.sort;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FieldValidatorLinkTest {

    private FieldValidatorLink validator = new FieldValidatorLink();
    private String[] paramsToCheck = {"gift_certificate.name", "create_date"};
    private String[] incParamsToCheck = {"", null, "asdfda"};

    @Test
    public void testValidation() {
        for (String param : paramsToCheck) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("field", param);
            assertTrue(validator.validate(paramMap));
        }
    }

    @Test
    public void testValidationIncParams() {
        for (String param : incParamsToCheck) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("field", param);
            assertFalse(validator.validate(paramMap));
        }
    }

}