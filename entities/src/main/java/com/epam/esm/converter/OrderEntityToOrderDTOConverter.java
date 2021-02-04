package com.epam.esm.converter;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrderEntityToOrderDTOConverter implements Function<Order, OrderDTO> {

    @Override
    public OrderDTO apply(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTimeOfRelease(order.getPurchaseTime().toString());
        dto.setClosed(order.isClosed());
        dto.setIdUser(order.getIdUser());
        dto.setIdCertificate(order.getIdCertificate());
        return dto;
    }
}
