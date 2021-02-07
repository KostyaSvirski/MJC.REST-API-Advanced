package com.epam.esm.service.impl;

import com.epam.esm.OrderDao;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.OrderDTOToOrderEntityConverter;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.impl.OrderDaoImpl;
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

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
class OrderServiceImplTest {

    @Mock
    private final OrderDao dao = Mockito.mock(OrderDaoImpl.class);
    @Mock
    private final OrderEntityToOrderDTOConverter entityToDTOConverter =
            Mockito.mock(OrderEntityToOrderDTOConverter.class);
    @Mock
    private final OrderDTOToOrderEntityConverter dtoToEntityConverter =
            Mockito.mock(OrderDTOToOrderEntityConverter.class);
    @InjectMocks
    private OrderServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() throws DaoException, ServiceException {
        Mockito.when(dao.findAll(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(new Order(1)));
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new OrderDTO(1));
        List<OrderDTO> actual = service.findAll(1, 1);
        assertEquals(Collections.singletonList(new OrderDTO(1)), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindSpecificOrder(long id) throws DaoException, ServiceException {
        Mockito.when(dao.find(Mockito.eq(id)))
                .thenReturn(Collections.singletonList(new Order(id)));
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new OrderDTO(id));
        Optional<OrderDTO> actual = service.find(id);
        assertEquals(Optional.of(new OrderDTO(id)), actual);
    }

    @Test
    public void testFindNotExistingSpecificOrder() throws DaoException, ServiceException {
        Mockito.when(dao.find(Mockito.anyLong()))
                .thenReturn(Collections.emptyList());
        Optional<OrderDTO> actual = service.find(0);
        assertEquals(Optional.empty(), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testCreation(long id) throws DaoException, ServiceException {
        Order order = new Order(id);
        Mockito.when(dtoToEntityConverter.apply(Mockito.any())).thenReturn(order);
        Mockito.when(dao.create(Mockito.any())).thenReturn((int) id);
        int actual = service.create(new OrderDTO(id, 1, 1));
        assertEquals(id, actual);
    }
}