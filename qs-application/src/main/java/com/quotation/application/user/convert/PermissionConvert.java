package com.quotation.application.user.convert;

import com.quotation.application.user.dto.PermissionDTO;
import com.quotation.domain.user.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermissionConvert {
    PermissionConvert INSTANCE = Mappers.getMapper(PermissionConvert.class);

    PermissionDTO toDTO(Permission permission);

    Permission toEntity(PermissionDTO dto);

    List<PermissionDTO> toDTOList(List<Permission> permissionList);
}
