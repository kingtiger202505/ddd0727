package com.quotation.application.convert;

import com.quotation.application.dto.inventory.InventoryDTO;
import com.quotation.domain.inventory.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryConvert {
    InventoryConvert INSTANCE = Mappers.getMapper(InventoryConvert.class);

    InventoryDTO toDTO(Inventory inventory);

    Inventory toEntity(InventoryDTO dto);

    List<InventoryDTO> toDTOList(List<Inventory> inventoryList);
}
