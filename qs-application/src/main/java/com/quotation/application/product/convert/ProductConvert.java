package com.quotation.application.product.convert;

import com.quotation.application.dto.product.ProductDTO;
import com.quotation.domain.product.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductConvert {
    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);

    ProductDTO toDTO(Product product);

    Product toEntity(ProductDTO dto);

    List<ProductDTO> toDTOList(List<Product> productList);
}
