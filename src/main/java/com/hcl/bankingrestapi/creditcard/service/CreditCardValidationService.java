package com.hcl.bankingrestapi.creditcard.service;

import com.hcl.bankingrestapi.creditcard.dto.CreditCardSpendDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCard;
import com.hcl.bankingrestapi.creditcard.enums.CreditCardErrorMessage;
import com.hcl.bankingrestapi.creditcard.service.entityservice.CreditCardEntityService;
import com.hcl.bankingrestapi.general.enums.GenStatusType;
import com.hcl.bankingrestapi.general.exception.GenBusinessException;
import com.hcl.bankingrestapi.general.exception.IllegalFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditCardValidationService {

    private final CreditCardEntityService creditCardEntityService;

    public void validateCardLimit(BigDecimal currentAvailableLimit) {
        if (currentAvailableLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new GenBusinessException(CreditCardErrorMessage.INSUFFICIENT_CREDIT_CARD_LIMIT);
        }
    }

    public void validateCreditCard(CreditCard creditCard) {
        if (creditCard == null) {
            throw new GenBusinessException(CreditCardErrorMessage.INVALID_CREDIT_CARD);
        }
        if (creditCard.getExpireDate().isBefore(LocalDate.now())) {
            throw new GenBusinessException(CreditCardErrorMessage.CREDIT_CARD_EXPIRED);
        }
    }

    public void isCutOffDayValid(Integer cutOffDay) {
        if (cutOffDay < 1 || cutOffDay > 31) {
            throw new IllegalFieldException(CreditCardErrorMessage.CUT_OFF_DAY_IS_NOT_VALID);
        }
    }

    public void controlIsEarningNotNegative(BigDecimal earning) {
        if (earning.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalFieldException(CreditCardErrorMessage.EARNING_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlAreFieldsNull(BigDecimal earning, Integer cutOffDay) {
        if (earning == null || cutOffDay == null) {
            throw new IllegalFieldException(CreditCardErrorMessage.FIELDS_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlAreFieldsNull(Long creditCardId, BigDecimal amount) {
        if (creditCardId == null || amount == null) {
            throw new IllegalFieldException(CreditCardErrorMessage.FIELDS_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlAreFieldsNull(CreditCardSpendDto creditCardSpendDto) {
        Long cardNo = creditCardSpendDto.getCardNo();
        Long cvvNo = creditCardSpendDto.getCvvNo();
        LocalDate expireDate = creditCardSpendDto.getExpireDate();
        BigDecimal amount = creditCardSpendDto.getAmount();
        String description = creditCardSpendDto.getDescription();
        boolean hasNull = cardNo == null || cvvNo == null || expireDate == null || amount == null || description.isBlank();

        if (hasNull) {
            throw new IllegalFieldException(CreditCardErrorMessage.FIELDS_CANNOT_BE_NEGATIVE);
        }

    }

    public void controlIsCardCancelled(GenStatusType genStatusType) {
        if (genStatusType == GenStatusType.PASSIVE) {
            throw new IllegalFieldException(CreditCardErrorMessage.CREDIT_CARD_CANCELLED);
        }
    }
}

