package com.epam.esm.converter;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserEntityToUserDTOConverter implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        return dto;
    }
}
