package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.persistence.UserEntity;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserEntityToUserDTOConverter implements Function<UserEntity, UserDTO> {

    @Override
    public UserDTO apply(UserEntity user) {
        return UserDTO.builder().name(user.getName())
                .surname(user.getSurname())
                .id(user.getId()).build();
    }
}
