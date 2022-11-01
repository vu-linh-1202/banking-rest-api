package com.hcl.bankingrestapi.creditcard.entity;

import com.hcl.bankingrestapi.creditcard.enums.CreditCardActivityType;
import com.hcl.bankingrestapi.general.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "CRD_CREDIT_CARD_ACTIVITY")
public class CreditCardActivity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "CreditCardActivity", sequenceName = "CRD_CREDIT_CARD_ACTIVITY_ID_SEQ")
    @GeneratedValue(generator = "CreditCardActivity")
    private Long id;

    @Column(name = "ID_CRD_CREDIT_CARD", nullable = false)
    private Long creditCardId;

    @Column(name = "AMOUNT", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "DESCRIPTION", length = 100, nullable = false)
    private String description;

    @Column(name = "CARD_ACTIVITY_TYPE", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private CreditCardActivityType cardActivityType;
}
