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

    private final OrderDao dao;
    private final OrderEntityToOrderDTOConverter toOrderDTOConverter;
    private final OrderDTOToOrderEntityConverter toOrderEntityConverter;

    @Autowired
    public OrderServiceImpl(OrderDao dao, OrderEntityToOrderDTOConverter toOrderDTOConverter,
                            OrderDTOToOrderEntityConverter toOrderEntityConverter) {
        this.dao = dao;
        this.toOrderDTOConverter = toOrderDTOConverter;
        this.toOrderEntityConverter = toOrderEntityConverter;
    }

    @Override
    public List<OrderDTO> findAll(int limit, int page) throws ServiceException {
        try {
            List<Order> listFromDao = dao.findAll(limit, page);
            return listFromDao.stream().map(toOrderDTOConverter)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }
    }

    @Override
    public Optional<OrderDTO> find(long id) throws ServiceException {
        try {
            List<Order> listFromDao = dao.find(id);
            return listFromDao.stream().map(toOrderDTOConverter)
                    .findFirst();
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }

    }

    @Override
    public int create(OrderDTO newOrder) throws ServiceException {
        PreparedValidatorChain<OrderDTO> chain = new IntermediateOrderLink();
        chain.linkWith(new IdCertificateValidatorLink()).linkWith(new IdUserValidatorLink());
        if (chain.validate(newOrder)) {
            try {
                return dao.create(toOrderEntityConverter.apply(newOrder));
            } catch (DaoException e) {
                throw new ServiceException("exception in dao");
            }
        }
        return 0;
    }
}
