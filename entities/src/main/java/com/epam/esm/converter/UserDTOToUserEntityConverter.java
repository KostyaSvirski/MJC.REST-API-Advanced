package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.persistence.UserEntity;

import java.util.function.Function;

public class UserDTOToUserEntityConverter implements Function<UserDTO, UserEntity> {

    @Override
    public UserEntity apply(UserDTO userDTO) {
        return UserEntity.builder().name(userDTO.getName()).surname(userDTO.getSurname()).build();
    }
}
