package com.epam.esm.contoller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.hypermedia.ActionHypermedia;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {


    private static final int DEFAULT_LIMIT_INT = 5;
    private static final int DEFAULT_PAGE_INT = 1;
    private static final String DEFAULT_LIMIT = "5";
    private static final String DEFAULT_PAGE = "1";
    @Autowired
    private UserService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveAllUsers
            (@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
             @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        try {
            List<UserDTO> resultList = service.findAll(limit, page);
            for(UserDTO dto : resultList) {
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveSpecificUser(dto.getId()))
                        .withRel("user"));
            }
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .retrieveAllUsers(limit, page))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificUser(@PathVariable long id) {
        try {
            Optional<UserDTO> user = service.find(id, 1, 1);
            if (user.isPresent()) {
                UserDTO dto = user.get();
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveOrdersOfSpecificUser(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT, dto.getId()))
                        .withRel("orders"));
                return new ResponseEntity<>(dto, HttpStatus.OK);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia("no content");
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveSpecificUser(id))
                        .withSelfRel());
                return new ResponseEntity<>(actionHypermedia, HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .retrieveSpecificUser(id))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<?> retrieveOrdersOfSpecificUser
            (@RequestParam(defaultValue = "5") int limit,
             @RequestParam(defaultValue = "1") int page,
             @PathVariable long id) {
        try {
            List<OrderDTO> resultList = service.findUsersOrders(id, limit, page);
            for(OrderDTO dto : resultList) {
                dto.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveOrderOfSpecificUser(id, dto.getId()))
                        .withRel("order"));
            }
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .retrieveOrdersOfSpecificUser(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT, id))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{idUser}/orders/{idOrder}")
    public ResponseEntity<?> retrieveOrderOfSpecificUser
            (@PathVariable long idUser, @PathVariable long idOrder) {
        try {
            Optional<OrderDTO> order = service.findSpecificOrderOfUser(idUser, idOrder, 1, 1);
            if (order.isPresent()) {
             /*   OrderDTO dto = order.get();*/
                // TODO: 04.02.2021
                return new ResponseEntity<>(order.get(), HttpStatus.OK);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia
                        ("not found with id of user" + idUser + "and id of order");
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveOrderOfSpecificUser(idUser, idOrder))
                        .withSelfRel());
                actionHypermedia.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .retrieveSpecificUser(idUser))
                        .withRel("user"));
                return new ResponseEntity<>(actionHypermedia, HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            ActionHypermedia actionHypermedia = new ActionHypermedia(e.getMessage());
            actionHypermedia.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                            .retrieveOrderOfSpecificUser(idUser, idOrder))
                    .withSelfRel());
            return new ResponseEntity<>(actionHypermedia, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
