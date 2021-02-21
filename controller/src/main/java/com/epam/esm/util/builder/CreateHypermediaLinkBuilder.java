package com.epam.esm.util.builder;

import com.epam.esm.contoller.GiftCertificateController;
import com.epam.esm.contoller.OrderController;
import com.epam.esm.contoller.TagController;
import com.epam.esm.contoller.UserController;
import com.epam.esm.dto.CreateActionHypermedia;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class CreateHypermediaLinkBuilder extends BuilderContainer<CreateActionHypermedia> {

    public CreateHypermediaLinkBuilder(CreateActionHypermedia hypermedia) {
        super(hypermedia);
    }

    public CreateHypermediaLinkBuilder buildNewCertificateLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .findSpecificCertificate(id))
                .withRel("certificate"));
        return this;
    }

    public CreateHypermediaLinkBuilder buildNewOrderLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveSpecificOrder(id))
                .withRel("certificate"));
        return this;
    }

    public CreateHypermediaLinkBuilder buildNewTagLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .findSpecificTag(id)
                )
                .withRel("new tag"));
        return this;
    }

}
