package com.epam.esm.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateDTO extends RepresentationModel<GiftCertificateDTO> {

    private long id;
    private String name;
    private String description;
    private long price;
    private int duration;
    private String createDate;
    private String lastUpdateDate;
    @Autowired
    private Set<TagDTO> tags;

    public <E> GiftCertificateDTO(String name, long price, int duration, HashSet<E> tags) {
        this.name = name;
        this.duration = duration;
        this.price = price;
        this.tags = (Set<TagDTO>) tags;
    }
}
