package com.epam.esm.validator.realisation.order;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.validator.realisation.IntermediateOrderLink;

public class IdCertificateValidatorLink extends IntermediateOrderLink {

    @Override
    public boolean validate(OrderDTO bean) {
        if(bean.getIdCertificate() <= 0) {
            return false;
        }
        return checkNextLink(bean);
    }
}
