package com.epam.esm.contoller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.hypermedia.CreateActionHypermedia;
import com.epam.esm.dto.hypermedia.ActionHypermedia;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final int DEFAULT_LIMIT_INT = 5;
    private static final int DEFAULT_PAGE_INT = 1;
    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";

    @Autowired
    OrderService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveAllOrders(@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
                                               @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        try {
            List<OrderDTO> resultList = service.findAll(limit, page);
            for (OrderDTO dto : resultList) {
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                                .retrieveSpecificOrder(dto.getId()))
                        .withRel("order"));
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveSpecificUser(dto.getIdUser()))
                        .withRel("user"));
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                                .findSpecificCertificate(dto.getIdCertificate()))
                        .withRel("certificate"));
            }
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                            .retrieveAllOrders(limit, page))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificOrder(@PathVariable long id) {
        try {
            Optional<OrderDTO> order = service.find(id);
            if (order.isPresent()) {
                OrderDTO dto = order.get();
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveSpecificUser(dto.getIdUser()))
                        .withRel("user"));
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                                .findSpecificCertificate(dto.getIdCertificate()))
                        .withRel("certificate"));
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia("not found with id" + id);
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                                .retrieveAllOrders(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT))
                        .withRel("orders"));
                return new ResponseEntity<>(actionHypermedia, HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                            .retrieveSpecificOrder(id))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> createNewOrder(@RequestBody OrderDTO order) {
        order.setTimeOfRelease(Instant.now().toString());
        try {
            int result = service.create(order);
            if (result != 0) {
                CreateActionHypermedia hypermedia = new CreateActionHypermedia(result);
                hypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveOrdersOfSpecificUser(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT, order.getIdUser()))
                        .withRel("orders of user"));
                return new ResponseEntity<>(hypermedia, HttpStatus.OK);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia("not valid data");
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                                .createNewOrder(order))
                        .withSelfRel());
                return new ResponseEntity<>(actionHypermedia, HttpStatus.BAD_REQUEST);
            }
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                            .createNewOrder(order))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
