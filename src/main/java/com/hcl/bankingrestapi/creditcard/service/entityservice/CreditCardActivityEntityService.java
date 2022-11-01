package com.hcl.bankingrestapi.creditcard.service.entityservice;

import com.hcl.bankingrestapi.creditcard.dto.CreditCardActivityAnalysisDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCardActivity;
import com.hcl.bankingrestapi.creditcard.repository.CreditCardActivityRepository;
import com.hcl.bankingrestapi.general.service.BaseEntityService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CreditCardActivityEntityService extends BaseEntityService<CreditCardActivity, CreditCardActivityRepository> {

    public CreditCardActivityEntityService(CreditCardActivityRepository repository) {
        super(repository);
    }

    public List<CreditCardActivity> findCreditCardActivityByAmountInterval(BigDecimal min, BigDecimal max) {
        List<CreditCardActivity> creditCardActivityList = getDao().findAllByAmountBetween(min, max);
        return creditCardActivityList;
    }

    public List<CreditCardActivity> findCreditCardActivityBetweenDates(Long creditCardId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return getDao().findAllByCrdCreditCardIdAndTransactionDateBetween(creditCardId, startDateTime, endDateTime);
    }

    public List<CreditCardActivity> findCreditCardActivityBetweenDates(Long creditCardId, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                                                       Optional<Integer> pageOptional, Optional<Integer> sizeOptional) {
        PageRequest pageRequest = getPageRequest(pageOptional, sizeOptional);
        return getDao().findAllByCreditCardIdAndTransactionDateBetween(creditCardId, startDateTime, endDateTime, pageRequest).toList();
    }

    public List<CreditCardActivityAnalysisDto> getCardActivityAnalysis(Long creditCardId) {
        List<CreditCardActivityAnalysisDto> creditCardActivityAnalysisDtoList = getDao().getCardActivityAnalysis(creditCardId);
        return creditCardActivityAnalysisDtoList;
    }

    public List<CreditCardActivity> findAllByCrdCreditCardIdAndTransactionDateBetween(Long creditCardId,
                                                                                      LocalDateTime termStartDate,
                                                                                      LocalDateTime termEndDate) {
        return getDao().findAllByCrdCreditCardIdAndTransactionDateBetween(creditCardId, termStartDate, termEndDate);
    }

}
