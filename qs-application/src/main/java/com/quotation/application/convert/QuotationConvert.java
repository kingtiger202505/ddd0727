package com.quotation.application.convert;

import com.quotation.application.dto.CreateQuotationCommand;
import com.quotation.application.dto.QuotationDTO;
import com.quotation.domain.quotation.model.Quotation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuotationConvert {
    QuotationConvert INSTANCE = Mappers.getMapper(QuotationConvert.class);

    QuotationDTO toDTO(Quotation quotation);

    Quotation toEntity(CreateQuotationCommand command);

    List<QuotationDTO> toDTOList(List<Quotation> quotationList);
}
