package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;

import java.util.function.Function;

public class UserDTOToUserEntityConverter implements Function<UserDTO, User> {

    @Override
    public User apply(UserDTO userDTO) {
        return new User.UserBuilder().buildId(userDTO.getId())
                .buildName(userDTO.getName())
                .buildSurname(userDTO.getSurname())
                .finishBuilding();
    }
}
