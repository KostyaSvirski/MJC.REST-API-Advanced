package com.epam.esm.util.builder;

import com.epam.esm.contoller.GiftCertificateController;
import com.epam.esm.dto.TagDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

// TODO: 16.02.2021
public class TagLinkBuilder extends BuilderContainer<TagDTO> {

    public TagLinkBuilder(TagDTO hypermedia) {
        super(hypermedia);
    }

    public TagLinkBuilder buildCertificatesDependsOnTagLink() {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .retrieveCertificates(5, 1, null, null, hypermedia.getName(),
                                null, null)
                )
                .withRel("certificates"));
        return this;
    }
}
