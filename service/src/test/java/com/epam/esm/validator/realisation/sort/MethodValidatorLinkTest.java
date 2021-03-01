package com.epam.esm.validator.realisation.sort;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MethodValidatorLinkTest {

    private MethodValidatorLink validator = new MethodValidatorLink();
    private String[] paramsToCheck = {"asc", "desc"};
    private String[] incParamsToCheck = {"", null, "asdfda"};

    @Test
    public void testValidation() {
        for (String param : paramsToCheck) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("method", param);
            assertTrue(validator.validate(paramMap));
        }
    }

    @Test
    public void testValidationIncParams() {
        for (String param : incParamsToCheck) {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("method", param);
            assertFalse(validator.validate(paramMap));
        }
    }

}