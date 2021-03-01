package com.epam.esm.validator.realisation;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.validator.PreparedValidatorChain;

public class IntermediateOrderLink extends PreparedValidatorChain<OrderDTO> {

    @Override
    public boolean validate(OrderDTO bean) {
        if (bean == null) {
            return false;
        }
        if (bean.getIdCertificate() == 0 || bean.getIdUser() == 0) {
            return false;
        }
        return checkNextLink(bean);
    }
}
