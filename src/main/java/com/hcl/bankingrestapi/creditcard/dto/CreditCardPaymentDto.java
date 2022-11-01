package com.hcl.bankingrestapi.creditcard.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditCardPaymentDto {
    private Long creditCardId;
    private BigDecimal amount;
}
