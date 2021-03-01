package com.epam.esm.validator.realisation;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.validator.PreparedValidatorChain;

public class IntermediateTagLink extends PreparedValidatorChain<TagDTO> {

    @Override
    public boolean validate(TagDTO bean) {
        if(bean == null) {
            return false;
        }
        if(bean.getName() == null || bean.getName().isEmpty()) {
            return false;
        }
        return checkNextLink(bean);
    }
}
