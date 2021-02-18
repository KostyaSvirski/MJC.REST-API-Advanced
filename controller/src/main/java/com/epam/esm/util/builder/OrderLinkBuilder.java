package com.epam.esm.util.builder;

import com.epam.esm.contoller.GiftCertificateController;
import com.epam.esm.contoller.OrderController;
import com.epam.esm.contoller.UserController;
import com.epam.esm.dto.OrderDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class OrderLinkBuilder extends BuilderContainer<OrderDTO> {

    public OrderLinkBuilder(OrderDTO hypermedia) {
        super(hypermedia);
    }

    public OrderLinkBuilder buildRetrieveSpecificOrderLink() {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveSpecificOrder(hypermedia.getId()))
                .withRel("order"));
        return this;
    }

    public OrderLinkBuilder buildUserReferenceLink() {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveSpecificUser(hypermedia.getIdUser()))
                .withRel("user"));
        return this;
    }

    public OrderLinkBuilder buildCertificateReferenceLink() {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .findSpecificCertificate(hypermedia.getIdCertificate()))
                .withRel("certificate"));
        return this;
    }

    public OrderLinkBuilder buildRetrieveOrderOfSpecificUserLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveOrderOfSpecificUser(id, hypermedia.getId()))
                .withRel("order"));
        return this;
    }

}
