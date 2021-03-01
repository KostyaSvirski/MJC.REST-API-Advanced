package com.epam.esm.validator.realisation.tag;

import com.epam.esm.dto.TagDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagNameValidatorLinkTest {

    private TagNameValidatorLink validator = new TagNameValidatorLink();
    private String[] paramsToCheck = {"asdf", "as asdsad s", "-a-dc1c", "basbc - AAAAA", "CSAC"};
    private String[] incParamsToCheck = {"asdfghjklpoiuytrewqasdfgvcxzxcvbnvjgyftrertyjhgfdsdfgchvfds"};

    @Test
    public void testValidation() {
        for (String param : paramsToCheck) {
            TagDTO tag = new TagDTO();
            tag.setName(param);
            assertTrue(validator.validate(tag));
        }
    }

    @Test
    public void testIncParams() {
        for (String param : incParamsToCheck) {
            TagDTO tag = new TagDTO();
            tag.setName(param);
            assertFalse(validator.validate(tag));
        }
    }

}