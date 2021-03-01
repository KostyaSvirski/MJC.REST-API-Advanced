package com.epam.esm.converter;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.persistence.OrderEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderEntityToOrderDTOConverter implements Function<OrderEntity, OrderDTO> {

    @Override
    public OrderDTO apply(OrderEntity order) {
        return OrderDTO.builder().id(order.getId()).idCertificate(order.getIdCertificate())
                .idUser(order.getIdUser())
                .isClosed(order.isClosed())
                .purchaseTime(order.getPurchaseTime().toString())
                .build();
    }
}
