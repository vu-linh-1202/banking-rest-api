package com.hcl.bankingrestapi.security.dto;

import lombok.Data;

@Data
public class SecurityLoginRequestDto {
    private Long identityNo;
    private String password;
}
