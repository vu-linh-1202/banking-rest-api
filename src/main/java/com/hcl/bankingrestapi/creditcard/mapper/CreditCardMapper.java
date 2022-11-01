package com.hcl.bankingrestapi.creditcard.mapper;

import com.hcl.bankingrestapi.creditcard.dto.CreditCardActivityDto;
import com.hcl.bankingrestapi.creditcard.dto.CreditCardDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCard;
import com.hcl.bankingrestapi.creditcard.entity.CreditCardActivity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CreditCardMapper {

    CreditCardMapper INSTANCE = Mappers.getMapper(CreditCardMapper.class);

    List<CreditCardDto> convertToCreditCardDtoList(List<CreditCard> creditCardList);

    List<CreditCardActivityDto> convertToCreditCardActivityDtoList(List<CreditCardActivity> creditCardActivityList);

    CreditCardDto convertToCreditCardDto(CreditCard creditCard);

    CreditCardActivityDto convertToCreditCardActivityDto(CreditCardActivity creditCardActivity);
}
