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
public class UserDTO extends RepresentationModel<UserDTO> {

    private long id;
    private String name;
    private String surname;

}
