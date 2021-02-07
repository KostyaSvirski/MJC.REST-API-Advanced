package com.epam.esm.service.impl;

import com.epam.esm.OrderDao;
import com.epam.esm.UserDao;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.impl.OrderDaoImpl;
import com.epam.esm.impl.UserDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
class UserServiceImplTest {

    @Mock
    private final OrderDao daoOrder = Mockito.mock(OrderDaoImpl.class);
    @Mock
    private final UserDao daoUser = Mockito.mock(UserDaoImpl.class);
    @Mock
    private final UserEntityToUserDTOConverter toUserDTOConverter =
            Mockito.mock(UserEntityToUserDTOConverter.class);
    @Mock
    private final OrderEntityToOrderDTOConverter toOrderDTOConverter =
            Mockito.mock(OrderEntityToOrderDTOConverter.class);
    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() throws DaoException, ServiceException {
        Mockito.when(daoUser.findAll(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(new User(1)));
        Mockito.when(toUserDTOConverter.apply(Mockito.any())).thenReturn(new UserDTO(1));
        List<UserDTO> actual = service.findAll(Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new UserDTO(1)), actual);
    }

    @Test
    public void testFindAllException() throws DaoException {
        Mockito.when(daoUser.findAll(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new DaoException());
        Throwable throwable = assertThrows(ServiceException.class,
                () -> service.findAll(Mockito.anyInt(), Mockito.anyInt()));
        String expected = "exception in dao";
        assertEquals(expected, throwable.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindSpecificUser(long id) throws DaoException, ServiceException {
        Mockito.when(daoUser.find(Mockito.eq(id)))
                .thenReturn(Collections.singletonList(new User(id)));
        Mockito.when(toUserDTOConverter.apply(Mockito.any())).thenReturn(new UserDTO(id));
        Optional<UserDTO> actual = service.find(id);
        assertEquals(Optional.of(new UserDTO(id)), actual);
    }

    @Test
    public void testFindSpecificUserNotFound() throws DaoException, ServiceException {
        Mockito.when(daoUser.find(Mockito.anyLong())).thenReturn(Collections.emptyList());
        Optional<UserDTO> actual = service.find(Mockito.anyLong());
        assertEquals(Optional.empty(), actual);
    }

    // FIXME: 05.02.2021
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindOrdersOfUser(long id) throws DaoException, ServiceException {
        OrderDTO dto = new OrderDTO();
        dto.setIdUser(id);
        Order entity = new Order();
        entity.setIdUser(id);
        Mockito.when(daoOrder.findOrdersOfSpecificUser(Mockito.eq(id), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(entity));
        Mockito.when(toOrderDTOConverter.apply(Mockito.any())).thenReturn(dto);
        List<OrderDTO> actual = service.findOrdersOfUser(Mockito.eq(id), Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(dto), actual);
    }

    @Test
    public void testFindSpecificOrderOfUser() throws DaoException, ServiceException {
        Mockito.when(daoOrder.findOrderOfSpecificUser(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Collections.singletonList(new Order()));
        Mockito.when(toOrderDTOConverter.apply(Mockito.any())).thenReturn(new OrderDTO());
        Optional<OrderDTO> actual = service.findSpecificOrderOfUser(Mockito.anyLong(), Mockito.anyLong());
        assertEquals(Optional.of(new OrderDTO()), actual);
    }

}