package com.epam.esm.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private long id;
    private long idUser;
    private long idCertificate;
    private String purchaseTime;
    private long cost;
    private boolean isClosed;

}
