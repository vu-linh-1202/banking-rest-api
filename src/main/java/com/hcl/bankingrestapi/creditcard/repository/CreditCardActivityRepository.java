package com.hcl.bankingrestapi.creditcard.repository;

import com.hcl.bankingrestapi.creditcard.dto.CreditCardActivityAnalysisDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCardActivity;
import lombok.Lombok;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CreditCardActivityRepository  extends JpaRepository<CreditCardActivity, Long> {

    List<CreditCardActivity> findAllByAmountBetween(BigDecimal min, BigDecimal max);

    List<CreditCardActivity> findAllByCrdCreditCardIdAndTransactionDateBetween(Long creditCardId, LocalDateTime startDate, LocalDateTime endDate);

    Page<CreditCardActivity> findAllByCreditCardIdAndTransactionDateBetween(
            Long creditCardId, LocalDateTime startDateTime, LocalDateTime endDateTime,
            PageRequest pageRequest
    );

    @Query(value = "SELECT"+
    "new com.hcl.bankingrestapi.creditcard.dto.CreditCardActivityAnalysisDto("+
            "activity.cardActivityType,"+
            "min(activity.amount),"+
            "max(activity.amount),"+
            "avg(activity.amount),"+
            "count(activity)" +
            " ) " +
            "FROM CreditCard card"+
            "LEFT JOIN CreditCardActivity activity"+
            "ON card.id = activity.creditCardId"+
            "WHERE card.id=:creditCardId"+
            "GROUP BY activity.cardActivityType"
    )
    List<CreditCardActivityAnalysisDto> getCardActivityAnalysis(@Param("creditCardId") Long creditCardId);
}
