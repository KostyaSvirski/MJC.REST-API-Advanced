package com.epam.esm.service.impl;

import com.epam.esm.OrderDao;
import com.epam.esm.UserDao;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final OrderDao daoOrder;
    private final UserDao daoUser;
    private final UserEntityToUserDTOConverter toUserDTOConverter;
    private final OrderEntityToOrderDTOConverter toOrderDTOConverter;

    @Autowired
    public UserServiceImpl(OrderDao daoOrder, UserDao daoUser,
                           UserEntityToUserDTOConverter toUserDTOConverter,
                           OrderEntityToOrderDTOConverter toOrderDTOConverter) {
        this.daoOrder = daoOrder;
        this.daoUser = daoUser;
        this.toUserDTOConverter = toUserDTOConverter;
        this.toOrderDTOConverter = toOrderDTOConverter;
    }

    @Override
    public List<UserDTO> findAll(int limit, int page) throws ServiceException {
        try {
            List<User> listFromDao = daoUser.findAll(limit, page);
            return listFromDao.stream().map(toUserDTOConverter)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }
    }

    @Override
    public Optional<UserDTO> find(long id) throws ServiceException {
        try {
            List<User> listFromDao = daoUser.find(id);
            return listFromDao.stream().map(toUserDTOConverter)
                    .findFirst();
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }
    }

    @Override
    public List<OrderDTO> findOrdersOfUser(long idUser, int limit, int page) throws ServiceException {
        try {
            List<Order> listFromDao = daoOrder.findOrdersOfSpecificUser(idUser, limit, page);
            return listFromDao.stream().map(toOrderDTOConverter)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }
    }

    @Override
    public Optional<OrderDTO> findSpecificOrderOfUser(long idUser, long idOrder)
            throws ServiceException {
        try {
            List<Order> listFromDao = daoOrder.findOrderOfSpecificUser(idUser, idOrder);
            return listFromDao.stream().map(toOrderDTOConverter)
                    .findFirst();
        } catch (DaoException e) {
            throw new ServiceException("exception in dao");
        }
    }
}
