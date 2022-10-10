package com.hcl.bankingrestapi.general.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class GenExceptionResponse {
    private Date errorDate;
    private String message;
    private String detail;
}
