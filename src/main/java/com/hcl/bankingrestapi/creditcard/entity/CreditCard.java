package com.hcl.bankingrestapi.creditcard.entity;

import com.hcl.bankingrestapi.general.entity.BaseEntity;
import com.hcl.bankingrestapi.general.enums.GenStatusType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="CRD_CREDIT_CARD")
public class CreditCard extends BaseEntity {

    @Id
    @SequenceGenerator(name = "CreditCard" , sequenceName = "CRD_CREDIT_CARD_ID_SEQ")
    @GeneratedValue(generator = "CreditCard")
    private Long id;

    @Column(name = "ID_CUS_CUSTOMER", nullable = false)
    private Long customerId;

    @Column(name = "CARD_NO", nullable = false,unique = true)
    private Long cardNo;

    @Column(name = "CVV_NO", nullable = false)
    private Long cvvNo;

    @Column(name = "EXPIRE_DATE", nullable = false)
    private LocalDate expireDate;

    @Column(name = "TOTAL_LIMIT", precision = 19, scale = 2)
    private BigDecimal totalLimit;

    @Column(name = "AVAILABLE_CARD_LIMIT", precision = 19, scale = 2)
    private BigDecimal availableCardLimit;

    @Column(name = "CURRENT_DEBT", precision = 19, scale = 2)
    private BigDecimal currentDebit;

    @Column(name = "MINIMUM_PAYMENT_AMOUNT", precision = 19, scale = 2)
    private BigDecimal minimumPaymentAmount;

    @Column(name = "CUTOFF_DATE", nullable = false)
    private LocalDate cutOffDate;

    @Column(name = "DUE_DATE", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_TYPE", length = 30, nullable = false)
    private GenStatusType statusType;

    @Column(name = "CANCEL_DATE")
    private LocalDateTime cancelDate;
}
