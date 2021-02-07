package com.epam.esm.converter;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.function.Function;

@Component
public class OrderDTOToOrderEntityConverter implements Function<OrderDTO, Order> {

    @Override
    public Order apply(OrderDTO orderDTO) {
        return new Order.OrderBuilder().buildUser(orderDTO.getIdUser())
                .buildCertificates(orderDTO.getIdCertificate())
                .buildPurchaseTime(Instant.parse(orderDTO.getTimeOfRelease()))
                .buildIsClosed(orderDTO.isClosed())
                .finishBuilding();
    }
}
