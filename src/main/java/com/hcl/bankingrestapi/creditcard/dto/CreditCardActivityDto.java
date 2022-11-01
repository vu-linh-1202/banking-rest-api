package com.hcl.bankingrestapi.creditcard.dto;

import com.hcl.bankingrestapi.creditcard.enums.CreditCardActivityType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreditCardActivityDto {
    private Long id;
    private Long creditCardId;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
    private CreditCardActivityType cardActivityType;
}
