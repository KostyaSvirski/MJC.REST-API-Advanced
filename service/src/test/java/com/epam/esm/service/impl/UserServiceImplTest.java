package com.epam.esm.service.impl;

import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.hibernate.UserRepository;
import com.epam.esm.hibernate.impl.OrderRepositoryImpl;
import com.epam.esm.hibernate.impl.UserRepositoryImpl;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.converter.UserEntityToUserDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.persistence.OrderEntity;
import com.epam.esm.persistence.UserEntity;
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
    private final OrderRepository orderRepository = Mockito.mock(OrderRepositoryImpl.class);
    @Mock
    private final UserRepository userRepository = Mockito.mock(UserRepositoryImpl.class);
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
        Mockito.when(userRepository.findAll(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(new UserEntity().builder().id(1).build()));
        Mockito.when(toUserDTOConverter.apply(Mockito.any())).thenReturn(new UserDTO().builder().id(1).build());
        List<UserDTO> actual = service.findAll(Mockito.anyInt(), Mockito.anyInt());
        assertEquals(Collections.singletonList(new UserDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindAllException() throws DaoException {
        Mockito.when(userRepository.findAll(Mockito.anyInt(), Mockito.anyInt())).thenThrow(new DaoException());
        assertThrows(ServiceException.class,
                () -> service.findAll(Mockito.anyInt(), Mockito.anyInt()));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindSpecificUser(long id) throws DaoException, ServiceException {
        Mockito.when(userRepository.find(Mockito.eq(id)))
                .thenReturn(Optional.of(new UserEntity().builder().id(id).build()));
        Mockito.when(toUserDTOConverter.apply(Mockito.any())).thenReturn(new UserDTO().builder().id(1).build());
        Optional<UserDTO> actual = service.find(id);
        assertEquals(Optional.of(new UserDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindSpecificUserNotFound() throws DaoException, ServiceException {
        Mockito.when(userRepository.find(Mockito.anyLong())).thenReturn(Optional.empty());
        Optional<UserDTO> actual = service.find(Mockito.anyLong());
        assertEquals(Optional.empty(), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindOrdersOfUser(long id) throws DaoException, ServiceException {
        OrderDTO dto = new OrderDTO();
        dto.setIdUser(id);
        OrderEntity entity = new OrderEntity();
        entity.setIdUser(id);
        Mockito.when(toOrderDTOConverter.apply(Mockito.any())).thenReturn(dto);
        Mockito.when(orderRepository.findOrdersOfSpecificUser(id, 1, 1))
                .thenReturn(Collections.singletonList(entity));
        List<OrderDTO> actual = service.findOrdersOfUser(id, 1, 1);
        assertEquals(Collections.singletonList(dto), actual);
    }

    @Test
    public void testFindSpecificOrderOfUser() throws DaoException, ServiceException {
        Mockito.when(orderRepository.findOrderOfSpecificUser(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Collections.singletonList(new OrderEntity()));
        Mockito.when(toOrderDTOConverter.apply(Mockito.any())).thenReturn(new OrderDTO());
        Optional<OrderDTO> actual = service.findSpecificOrderOfUser(Mockito.anyLong(), Mockito.anyLong());
        assertEquals(Optional.of(new OrderDTO()), actual);
    }

}