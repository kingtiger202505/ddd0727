package com.quotation.application.user.convert;

import com.quotation.application.user.dto.RoleDTO;
import com.quotation.domain.user.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleConvert {
    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO dto);

    List<RoleDTO> toDTOList(List<Role> roleList);
}
