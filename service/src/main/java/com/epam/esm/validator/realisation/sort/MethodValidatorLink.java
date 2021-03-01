package com.epam.esm.validator.realisation.sort;

import com.epam.esm.validator.realisation.IntermediateSortLink;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MethodValidatorLink extends IntermediateSortLink {

    private static final String KEY = "method";

    @Override
    public boolean validate(Map<String, String> paramMap) {
        if(paramMap.get(KEY) == null) {
            return false;
        }
        Optional<String> result = Arrays.stream(TypesOfMethods.values())
                .map(Enum::name)
                .filter(nameOfMethod -> nameOfMethod.equals(paramMap.get(KEY).toUpperCase(Locale.ROOT)))
                .findFirst();
        if (result.isPresent()) {
            return checkNextLink(paramMap);
        }
        return false;
    }

    private enum TypesOfMethods {
        ASC,
        DESC
    }
}
