package com.epam.esm.service.impl;

import com.epam.esm.OrderDao;
import com.epam.esm.converter.OrderDTOToOrderEntityConverter;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.PreparedValidatorChain;
import com.epam.esm.validator.realisation.IntermediateOrderLink;
import com.epam.esm.validator.realisation.order.IdCertificateValidatorLink;
import com.epam.esm.validator.realisation.order.IdUserValidatorLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao dao;
    @Autowired
    private OrderEntityToOrderDTOConverter toOrderDTOConverter;
    @Autowired
    private OrderDTOToOrderEntityConverter toOrderEntityConverter;

    @Override
    public List<OrderDTO> findAll(int limit, int page) throws ServiceException {
        try {
            List<Order> listFromDao = dao.findAll(limit, page);
            return listFromDao.stream().map(entity -> toOrderDTOConverter.apply(entity))
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }
    }

    @Override
    public Optional<OrderDTO> find(long id, int limit, int page) throws ServiceException {
        try {
            List<Order> listFromDao = dao.find(id, limit, page);
            return listFromDao.stream().map(entity -> toOrderDTOConverter.apply(entity))
                    .findFirst();
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }

    }

    @Override
    public int create(OrderDTO newOrder) throws ServiceException {
        PreparedValidatorChain<OrderDTO> chain = new IntermediateOrderLink();
        chain.linkWith(new IdCertificateValidatorLink()).linkWith(new IdUserValidatorLink());
        if(chain.validate(newOrder)) {
            try {
                int idOfNewOrder = dao.create(toOrderEntityConverter.apply(newOrder));
                return idOfNewOrder;
            } catch (DaoException e) {
                throw new ServiceException("exception in dao");
            }
        }
        return 0;
    }
}
