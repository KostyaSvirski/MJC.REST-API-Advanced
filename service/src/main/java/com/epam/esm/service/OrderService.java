package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.ServiceException;

public interface OrderService extends BaseService<OrderDTO> {

    int create(OrderDTO newOrder) throws ServiceException;
}
