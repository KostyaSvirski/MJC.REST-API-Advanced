package com.epam.esm.converter;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.persistence.OrderEntity;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.function.Function;

@Component
public class OrderDTOToOrderEntityConverter implements Function<OrderDTO, OrderEntity> {

    @Override
    public OrderEntity apply(OrderDTO orderDTO) {
        return OrderEntity.builder().cost(orderDTO.getCost()).closed(orderDTO.isClosed())
                .idCertificate(orderDTO.getIdCertificate())
                .idUser(orderDTO.getIdUser())
                .purchaseTime(Timestamp.valueOf(orderDTO.getPurchaseTime()))
                .build();
    }
}
