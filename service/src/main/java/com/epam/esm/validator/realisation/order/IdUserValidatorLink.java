package com.epam.esm.validator.realisation.order;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.validator.realisation.IntermediateOrderLink;

public class IdUserValidatorLink extends IntermediateOrderLink {

    @Override
    public boolean validate(OrderDTO bean) {
        if(bean.getIdUser() <= 0) {
            return false;
        }
        return checkNextLink(bean);
    }
}
