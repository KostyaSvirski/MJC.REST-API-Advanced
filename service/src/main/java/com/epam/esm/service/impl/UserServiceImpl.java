package com.epam.esm.service.impl;

import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.persistence.OrderEntity;
import com.epam.esm.persistence.UserEntity;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserEntityToUserDTOConverter toUserDTOConverter;
    private final OrderEntityToOrderDTOConverter toOrderDTOConverter;

    @Autowired
    public UserServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                           UserEntityToUserDTOConverter toUserDTOConverter,
                           OrderEntityToOrderDTOConverter toOrderDTOConverter) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.toUserDTOConverter = toUserDTOConverter;
        this.toOrderDTOConverter = toOrderDTOConverter;
    }

    @Override
    public List<UserDTO> findAll(int limit, int page) {
        List<UserEntity> listFromDao = userRepository.findAll(limit, page);
        return listFromDao.stream().map(toUserDTOConverter)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> find(long id) {
        Optional<UserEntity> userFromDao = userRepository.find(id);
        return userFromDao.map(toUserDTOConverter);
    }

    @Override
    public List<OrderDTO> findOrdersOfUser(long idUser, int limit, int page) {
        List<OrderEntity> listFromDao = orderRepository.findOrdersOfSpecificUser(idUser, limit, page);
        return listFromDao.stream().map(toOrderDTOConverter)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> findSpecificOrderOfUser(long idUser, long idOrder) {
        List<OrderEntity> listFromDao = orderRepository.findOrderOfSpecificUser(idUser, idOrder);
        return listFromDao.stream().map(toOrderDTOConverter)
                .findFirst();

    }
}
