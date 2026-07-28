package com.quotation.application.product.convert;

import com.quotation.application.product.dto.SkuDTO;
import com.quotation.domain.product.model.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkuConvert {
    SkuConvert INSTANCE = Mappers.getMapper(SkuConvert.class);

    SkuDTO toDTO(Sku sku);

    Sku toEntity(SkuDTO dto);

    List<SkuDTO> toDTOList(List<Sku> skuList);
}
