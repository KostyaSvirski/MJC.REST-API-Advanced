package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService extends BaseService<UserDTO> {

    List<OrderDTO> findUsersOrders(long idUser, int limit, int page) throws ServiceException;
    Optional<OrderDTO> findSpecificOrderOfUser (long idUser, long idOrder, int limit, int page) throws ServiceException;

}
