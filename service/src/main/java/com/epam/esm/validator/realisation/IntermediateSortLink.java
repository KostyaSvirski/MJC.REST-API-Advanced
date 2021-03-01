package com.epam.esm.validator.realisation;

import com.epam.esm.validator.PreparedValidatorChain;

import java.util.Map;

public class IntermediateSortLink extends PreparedValidatorChain<Map<String, String>> {

    private static final String FIELD = "field";
    private static final String METHOD = "method";

    @Override
    public boolean validate(Map<String, String> paramMap) {
        if (paramMap.get(FIELD) != null && paramMap.get(METHOD) != null) {
            return checkNextLink(paramMap);
        }
        return false;
    }
}
