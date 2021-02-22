package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.validator.realisation.IntermediateCertificateLink;
import com.epam.esm.validator.realisation.tag.TagNameValidatorLink;

public class TagsValidatorLink extends IntermediateCertificateLink {

    @Override
    public boolean validate(GiftCertificateDTO bean) {
        TagNameValidatorLink validatorForTags = new TagNameValidatorLink();
        if (bean.getTags() == null) {
            return checkNextLink(bean);
        }
        for (TagDTO tag : bean.getTags()) {
            if (tag.getId() < 0 || !validatorForTags.validate(tag)) {
                return false;
            }
        }
        return checkNextLink(bean);
    }
}
