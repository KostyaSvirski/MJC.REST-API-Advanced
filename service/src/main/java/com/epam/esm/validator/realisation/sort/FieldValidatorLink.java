package com.epam.esm.validator.realisation.sort;

import com.epam.esm.validator.realisation.IntermediateSortLink;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class FieldValidatorLink extends IntermediateSortLink {

    private static final String KEY = "field";

    @Override
    public boolean validate(Map<String, String> paramMap) {
        if(paramMap.get(KEY) == null) {
            return false;
        }
        Optional<String> result = Arrays.stream(TypesOfFields.values())
                .map(TypesOfFields::getSimpleName)
                .filter(nameOfField -> nameOfField.equals(paramMap.get(KEY)))
                .findFirst();
        if (result.isPresent()) {
            return checkNextLink(paramMap);
        }
        return false;
    }

    private enum TypesOfFields {
        NAME("name_of_certificate"),
        CREATE_DATE("create_date");

        private String simpleName;

        TypesOfFields(String simpleName) {
            this.simpleName = simpleName;
        }

        public String getSimpleName() {
            return simpleName;
        }
    }
}
