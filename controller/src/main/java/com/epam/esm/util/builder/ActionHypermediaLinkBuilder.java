package com.epam.esm.util.builder;

import com.epam.esm.contoller.GiftCertificateController;
import com.epam.esm.contoller.OrderController;
import com.epam.esm.contoller.TagController;
import com.epam.esm.contoller.UserController;
import com.epam.esm.dto.ActionHypermedia;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class ActionHypermediaLinkBuilder extends BuilderContainer<ActionHypermedia> {

    public ActionHypermediaLinkBuilder(ActionHypermedia hypermedia) {
        super(hypermedia);
    }

    public ActionHypermediaLinkBuilder buildRetrieveAllCertificateSelfLink
            (int limit, int page, String partOfName, String partOfDescription,
             String nameOfTag, String field, String method) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .retrieveCertificates(limit, page, partOfName, partOfDescription, nameOfTag, field,
                                method))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveSpecificCertificateSelfLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .findSpecificCertificate(id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildCreateCertificateSelfLink(GiftCertificateDTO dto) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .createNewCertificate(dto))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildDeleteCertificateSelfLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .deleteCertificate(id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveSpecificCertificateLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .findSpecificCertificate(id))
                .withRel("certificate"));
        return this;
    }

    public ActionHypermediaLinkBuilder buildUpdateCertificateSelfLink(GiftCertificateDTO certificate, long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .updateCertificate(certificate, id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveAllOrdersLink(int limit, int page) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveAllOrders(limit, page))
                .withRel("orders"));
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveSpecificOrderSelfLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveSpecificOrder(id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildCreateOrderSelfLink(OrderDTO order) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .createNewOrder(order))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveAllUsersSelfLink(int limit, int page) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveAllUsers(limit, page))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveSpecificUserSelfLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveSpecificUser(id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveOrdersOfSpecificUserSelfLink(int limit, int page, long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveOrdersOfSpecificUser(limit, page, id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveOrderOfSpecificUserSelfLink(long idUser, long idOrder) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveOrderOfSpecificUser(idUser, idOrder))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildRetrieveSpecificUserLink(long idUser) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveSpecificUser(idUser))
                .withRel("user"));
        return this;
    }

    public ActionHypermediaLinkBuilder buildFindAllTagsSelfLink(int limit, int page) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .findAllTags(limit, page))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildFindSpecificTagSelfLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .findSpecificTag(id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildAddTagSelfLink(TagDTO newTag) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .addTag(newTag))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildDeleteTagSelfLink(long id) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .deleteTag(id))
                .withSelfRel());
        return this;
    }

    public ActionHypermediaLinkBuilder buildFindAllTagsLink(int limit, int page) {
        hypermedia.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .findAllTags(limit, page))
                .withRel("tags"));
        return this;
    }
}
