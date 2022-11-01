package com.hcl.bankingrestapi.creditcard.service.entityservice;

import com.hcl.bankingrestapi.creditcard.dto.CreditCardDetailsDto;
import com.hcl.bankingrestapi.creditcard.entity.CreditCard;
import com.hcl.bankingrestapi.creditcard.repository.CreditCardRepository;
import com.hcl.bankingrestapi.general.enums.GenStatusType;
import com.hcl.bankingrestapi.general.service.BaseEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CreditCardEntityService extends BaseEntityService<CreditCard, CreditCardRepository> {

    public CreditCardEntityService(CreditCardRepository repository) {
        super(repository);
    }

    public List<CreditCard> findAllByStatusType(GenStatusType statusType) {
        return getDao().findAllByStatusType(statusType);
    }

    public List<CreditCard> findAllActivityCreditCardList() {
        return getDao().findAllByStatusType(GenStatusType.ACTIVE);
    }

    public CreditCard findByCardNoAndCvvNoAndExpireDate(Long cardNo, Long cvvNo, LocalDate expireDate) {
        return getDao().findByCardNoAndCvvNoAndExpireDateAndStatusType(cardNo, cvvNo, expireDate, GenStatusType.ACTIVE);
    }

    public CreditCardDetailsDto getCreditCardDetails(Long creditCardId) {
        return getDao().getCreditCardDetails(creditCardId);
    }
}
