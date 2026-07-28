package com.quotation.application.user.convert;

import com.quotation.application.dto.user.UserDTO;
import com.quotation.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConvert {
    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserDTO toDTO(User user);

    User toEntity(UserDTO dto);

    List<UserDTO> toDTOList(List<User> userList);
}
