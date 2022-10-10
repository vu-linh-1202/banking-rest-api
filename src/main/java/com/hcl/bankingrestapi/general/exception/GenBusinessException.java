package com.hcl.bankingrestapi.general.exception;

import com.hcl.bankingrestapi.general.enums.BaseErrorMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@RequiredArgsConstructor
public class GenBusinessException extends RuntimeException {
    private final BaseErrorMessage baseErrorMessage;
}
