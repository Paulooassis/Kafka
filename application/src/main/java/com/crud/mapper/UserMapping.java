package com.crud.mapper;

import com.crud.dto.request.UserRequestDTO;
import com.crud.dto.response.UserResponseDTO;
import com.crud.entities.User;
import io.micronaut.context.annotation.Mapper;

@Mapper
public abstract class UserMapping {

    public abstract User toEntity(UserRequestDTO dto);
    public abstract UserResponseDTO toResponseDTO(User entity);
}
