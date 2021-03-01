package com.epam.esm.validator.realisation.tag;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.validator.realisation.IntermediateTagLink;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagNameValidatorLink extends IntermediateTagLink {

    private static final String REG_EXP_NAME = "^[A-Za-z 1-9-]+$";

    @Override
    public boolean validate(TagDTO bean) {
        if (bean.getName() == null || bean.getName().length() > 20) {
            return false;
        }
        Pattern pattern = Pattern.compile(REG_EXP_NAME);
        Matcher matcher = pattern.matcher(bean.getName());
        if (!matcher.matches()) {
            return false;
        }
        return checkNextLink(bean);
    }
}
