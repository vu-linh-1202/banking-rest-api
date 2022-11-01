package com.hcl.bankingrestapi.creditcard.service;


import com.hcl.bankingrestapi.creditcard.dto.CreditCardActivityAnalysisDto;
import com.hcl.bankingrestapi.creditcard.dto.CreditCardActivityDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCardActivity;
import com.hcl.bankingrestapi.creditcard.mapper.CreditCardMapper;
import com.hcl.bankingrestapi.creditcard.service.entityservice.CreditCardActivityEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CreditCardActivityService {

    private final CreditCardActivityEntityService creditCardActivityEntityService;
    private final CreditCardActivityValidationService creditCardActivityValidationService;

    public List<CreditCardActivityDto> findCreditCardActivityByAmountInterval(BigDecimal min, BigDecimal max) {
        creditCardActivityValidationService.controlIsParameterMinLargeThanMax(min, max);
        List<CreditCardActivity> creditCardActivityList = creditCardActivityEntityService.findCreditCardActivityByAmountInterval(min, max);

        List<CreditCardActivityDto> convertToCreditCardDtoList = CreditCardMapper.INSTANCE.convertToCreditCardActivityDtoList(creditCardActivityList);
        return convertToCreditCardDtoList;
    }

    public List<CreditCardActivityAnalysisDto> getCardActivityAnalysis(Long creditCardId) {
        creditCardActivityValidationService.controlIsCreditCardExist(creditCardId);

        List<CreditCardActivityAnalysisDto> creditCardActivityAnalysisDtoList = creditCardActivityEntityService.getCardActivityAnalysis(creditCardId);
        return creditCardActivityAnalysisDtoList;
    }
}
