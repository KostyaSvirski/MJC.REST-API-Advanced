package com.epam.esm.contoller;

import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.builder.ActionHypermediaLinkBuilder;
import com.epam.esm.util.builder.OrderLinkBuilder;
import com.epam.esm.util.builder.UserLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final String ERROR_MESSAGE = "error";
    @Autowired
    private UserService service;

    @GetMapping("/")
    public ResponseEntity<?> retrieveAllUsers
            (@RequestParam(defaultValue = DEFAULT_LIMIT) int limit,
             @RequestParam(defaultValue = DEFAULT_PAGE) int page) {
        try {
            List<UserDTO> resultList = service.findAll(limit, page);
            for (int i = 0; i < resultList.size(); i++) {
                UserLinkBuilder builder = new UserLinkBuilder(resultList.get(i));
                builder.buildRetrieveSpecificUserLink();
                resultList.set(i, builder.getHypermedia());
            }
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveAllUsersSelfLink(limit, page);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveSpecificUser(@PathVariable long id) {
        try {
            Optional<UserDTO> user = service.find(id);
            if (user.isPresent()) {
                UserLinkBuilder builder = new UserLinkBuilder(user.get());
                builder.buildOrdersReferencesLink(DEFAULT_LIMIT_INT, DEFAULT_PAGE_INT);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
            } else {
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia("no content"));
                builder.buildRetrieveSpecificUserSelfLink(id);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveSpecificUserSelfLink(id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<?> retrieveOrdersOfSpecificUser
            (@RequestParam(defaultValue = "5") int limit,
             @RequestParam(defaultValue = "1") int page,
             @PathVariable long id) {
        try {
            List<OrderDTO> resultList = service.findOrdersOfUser(id, limit, page);
            for (int i = 0; i < resultList.size(); i++) {
                OrderLinkBuilder builder = new OrderLinkBuilder(resultList.get(i));
                builder.buildRetrieveOrderOfSpecificUserLink(id);
                resultList.set(i, builder.getHypermedia());
            }
            return new ResponseEntity<>(resultList, HttpStatus.OK);
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveOrdersOfSpecificUserSelfLink(limit, page, id);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{idUser}/orders/{idOrder}")
    public ResponseEntity<?> retrieveOrderOfSpecificUser
            (@PathVariable long idUser, @PathVariable long idOrder) {
        try {
            Optional<OrderDTO> order = service.findSpecificOrderOfUser(idUser, idOrder);
            if (order.isPresent()) {
                OrderLinkBuilder builder = new OrderLinkBuilder(order.get());
                builder.buildUserReferenceLink().buildCertificateReferenceLink();
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.OK);
            } else {
                ActionHypermedia actionHypermedia = new ActionHypermedia
                        ("not found with id of user" + idUser + "and id of order");
                ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(actionHypermedia);
                builder.buildRetrieveOrderOfSpecificUserSelfLink(idUser, idOrder)
                        .buildRetrieveSpecificUserLink(idUser);
                return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            ActionHypermediaLinkBuilder builder = new ActionHypermediaLinkBuilder(new ActionHypermedia(ERROR_MESSAGE));
            builder.buildRetrieveOrderOfSpecificUserSelfLink(idUser, idOrder);
            return new ResponseEntity<>(builder.getHypermedia(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
