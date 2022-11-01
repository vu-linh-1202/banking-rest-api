package com.hcl.bankingrestapi.creditcard.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CreditCardDetailsDto {

    private final String customerName;
    private final String customerSurname;
    private final Long cardNo;
    private final LocalDate expireDate;
    private final BigDecimal currentDebt;
    private final BigDecimal minimumPaymentAmount;
    private final LocalDate cutoffDate;
    private final LocalDate dueDate;
    private List<CreditCardActivityDto> crdCreditCardActivityDtoList;
}
