package com.hcl.bankingrestapi.creditcard.dto;

import com.hcl.bankingrestapi.creditcard.enums.CreditCardActivityType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditCardActivityAnalysisDto {
    private CreditCardActivityType activityType;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Double avgAmount;
    private Long activityCount;
}
