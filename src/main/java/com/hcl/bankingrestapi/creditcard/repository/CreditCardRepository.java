package com.hcl.bankingrestapi.creditcard.repository;

import com.hcl.bankingrestapi.creditcard.dto.CreditCardDetailsDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCard;
import com.hcl.bankingrestapi.general.enums.GenStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    List<CreditCard> findAllByStatusType(GenStatusType statusType);

    CreditCard findByCardNoAndCvvNoAndExpireDateAndStatusType(Long cardNo, Long cvvNo, LocalDate expireDate, GenStatusType statusType);

    @Query(
            "select" +
                    "new com.hcl.bankingrestapi.creditcard.dto.CreditCardDetailsDto(" +
                    "customer.name," +
                    "customer.surname," +
                    "creditCard.cardNo," +
                    "creditCard.expireDate," +
                    "creditCard.currentDebit," +
                    "creditCard.minimumPaymentAmount," +
                    "creditCard.cutOffDate," +
                    "creditCard.dueDate" +
                    ")" +
                    "from CreditCard creditCard" +
                    "left join Customer customer on creditCard.customerId=customer.id" +
                    "where creditCard.id = :creditCardId"
    )
    CreditCardDetailsDto getCreditCardDetails(Long creditCardId);
}
