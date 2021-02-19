package com.epam.esm.validator.realisation.certificate;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.validator.realisation.IntermediateCertificateLink;

public class DurationValidatorLink extends IntermediateCertificateLink {

    @Override
    public boolean validate(GiftCertificateDTO bean) {
        if (bean.getDuration() == null) {
            return checkNextLink(bean);
        }
        if (bean.getDuration() < 0) {
            return false;
        }
        return checkNextLink(bean);
    }
}
