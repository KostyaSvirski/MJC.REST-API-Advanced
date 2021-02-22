package com.epam.esm.service.impl;

import com.epam.esm.hibernate.OrderRepository;
import com.epam.esm.hibernate.impl.OrderRepositoryImpl;
import com.epam.esm.config.ServiceConfig;
import com.epam.esm.converter.OrderDTOToOrderEntityConverter;
import com.epam.esm.converter.OrderEntityToOrderDTOConverter;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.exception.DaoException;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.persistence.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = ServiceConfig.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceImplTest {

    @Mock
    private final OrderRepository repository = Mockito.mock(OrderRepositoryImpl.class);
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
    public void testFindAll() {
        Mockito.when(repository.findAll(1, 1))
                .thenReturn(Collections.singletonList(new OrderEntity().builder().id(1).build()));
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new OrderDTO().builder().id(1).build());
        List<OrderDTO> actual = service.findAll(1, 1);
        assertEquals(Collections.singletonList(new OrderDTO().builder().id(1).build()), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testFindSpecificOrder(long id) throws DaoException, ServiceException {
        Mockito.when(repository.find(Mockito.eq(id)))
                .thenReturn(Optional.of(new OrderEntity().builder().id(id).build()));
        Mockito.when(entityToDTOConverter.apply(Mockito.any())).thenReturn(new OrderDTO().builder().id(1).build());
        Optional<OrderDTO> actual = service.find(id);
        assertEquals(Optional.of(new OrderDTO().builder().id(1).build()), actual);
    }

    @Test
    public void testFindNotExistingSpecificOrder() throws DaoException, ServiceException {
        Mockito.when(repository.find(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Optional<OrderDTO> actual = service.find(0);
        assertEquals(Optional.empty(), actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void testCreation(long id) throws DaoException, ServiceException {
        OrderEntity order = new OrderEntity();
        order.setId(id);
        Mockito.when(dtoToEntityConverter.apply(Mockito.any())).thenReturn(order);
        Mockito.when(repository.create(Mockito.any())).thenReturn((int) id);
        int actual = service.create(new OrderDTO().builder().id(id).idCertificate(1).idUser(1).build());
        assertEquals(id, actual);
    }
}