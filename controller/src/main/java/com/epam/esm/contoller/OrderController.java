package com.epam.esm.contoller;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.CreateActionHypermedia;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.builder.ActionHypermediaLinkBuilder;
import com.epam.esm.util.builder.CreateHypermediaLinkBuilder;
import com.epam.esm.util.builder.OrderLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final int DEFAULT_LIMIT_INT = 5;
    private static final int DEFAULT_PAGE_INT = 1;
    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";
    private static final String ERROR_MESSAGE = "error";

    @Autowired
    private OrderService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveAllOrders(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                               @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        try {
            List<OrderDTO> resultList = service.findAll(limit, page);
            for (int i = 0; i < resultList.size(); i++) {
                OrderLinkBuilder builder = new OrderLinkBuilder(resultList.get(i));
                builder.buildCertificateReferenceLink().buildUserReferenceLink().buildRetrieveSpecificOrderLink();
                resultList.set(i, builder.getHypermedia());
            }
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveAllOrdersLink(limit, page);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificOrder(@PathVariable long id) {
        try {
            Optional<OrderDTO> order = service.find(id);
            if (order.isPresent()) {
                OrderLinkBuilder builder = new OrderLinkBuilder(order.get());
                builder.buildUserReferenceLink().buildCertificateReferenceLink();
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia("not found with id" + id);
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(actionHypermedia);
                builder.buildRetrieveAllOrdersLink(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
            }
        } catch (Throwable e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(ERROR_MESSAGE);
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(actionHypermedia);
            builder.buildRetrieveSpecificOrderSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO order) {
        order.setPurchaseTime(LocalDateTime.now().toString());
        try {
            int result = service.create(order);
            if (result != 0) {
                CreateHypermediaLinkBuilder builder = new CreateHypermediaLinkBuilder(new CreateActionHypermedia(result));
                builder.buildNewOrderLink(result);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
            } else {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia("not valid data"));
                builder.buildCreateOrderSelfLink(order);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.BAD_REQUEST);
            }
        } catch (Throwable e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder
                    (new ActionHypermedia(ERROR_MESSAGE));
            builder.buildCreateOrderSelfLink(order);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
