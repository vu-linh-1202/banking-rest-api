package com.hcl.bankingrestapi.customer.dto;

import lombok.Data;

@Data
public class CustomerSaveDto {
    private String name;
    private String surname;
    private String identityNo;
    private String password;
}
