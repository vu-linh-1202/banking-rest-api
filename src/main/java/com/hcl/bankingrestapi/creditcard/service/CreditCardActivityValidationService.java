package com.hcl.bankingrestapi.creditcard.service;

import com.hcl.bankingrestapi.creditcard.enums.CreditCardErrorMessage;
import com.hcl.bankingrestapi.creditcard.service.entityservice.CreditCardEntityService;
import com.hcl.bankingrestapi.general.exception.IllegalFieldException;
import com.hcl.bankingrestapi.general.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditCardActivityValidationService {

    private final CreditCardEntityService creditCardEntityService;

    public void controlIsParameterMinLargeThanMax(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new IllegalFieldException(CreditCardErrorMessage.PARAMETER_MIN_CANNOT_BE_LARGER_THAN_MAX);
        }
    }

    public void controlIsCreditCardExist(Long creditCardId) {
        creditCardEntityService.findById(creditCardId).orElseThrow(() ->
                new ItemNotFoundException(CreditCardErrorMessage.CREDIT_CARD_NOT_FOUND));
    }
}
