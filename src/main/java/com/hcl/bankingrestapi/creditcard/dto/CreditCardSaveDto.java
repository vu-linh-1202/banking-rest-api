package com.hcl.bankingrestapi.creditcard.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreditCardSaveDto {

    @NotNull
    private BigDecimal earning;

    private Integer cutOffDay;
}
