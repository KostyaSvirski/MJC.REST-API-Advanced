package com.epam.esm.util.builder;

import com.epam.esm.contoller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class CertificateLinkBuilder extends BuilderContainer<GiftCertificateDTO>{

    public CertificateLinkBuilder(GiftCertificateDTO dto) {
        super(dto);
    }

    public CertificateLinkBuilder buildCertificateHypermedia() {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .findSpecificCertificate(hypermedia.getId()))
                .withRel("certificate"));
        return this;
    }
}
