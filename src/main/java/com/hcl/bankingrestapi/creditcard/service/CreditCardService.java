package com.hcl.bankingrestapi.creditcard.service;

import com.hcl.bankingrestapi.creditcard.dto.*;
import com.hcl.bankingrestapi.creditcard.entity.CreditCard;
import com.hcl.bankingrestapi.creditcard.entity.CreditCardActivity;
import com.hcl.bankingrestapi.creditcard.enums.CreditCardActivityType;
import com.hcl.bankingrestapi.creditcard.mapper.CreditCardMapper;
import com.hcl.bankingrestapi.creditcard.service.entityservice.CreditCardActivityEntityService;
import com.hcl.bankingrestapi.creditcard.service.entityservice.CreditCardEntityService;
import com.hcl.bankingrestapi.general.enums.GenStatusType;
import com.hcl.bankingrestapi.general.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreditCardService {

    private final CreditCardEntityService creditCardEntityService;
    private final CreditCardValidationService creditCardValidationService;
    private final CreditCardActivityEntityService creditCardActivityEntityService;

    /**
     * Find all credit cards
     *
     * @return creditCardResponseDtoList
     */
    public List<CreditCardDto> findAllCreditCards() {
        List<CreditCard> creditCardList = creditCardEntityService.findAllActivityCreditCardList();
        List<CreditCardDto> creditCardResponseDtoList = CreditCardMapper.INSTANCE.convertToCreditCardDtoList(creditCardList);

        log.info("Find all credit card success: {}", creditCardResponseDtoList);
        return creditCardResponseDtoList;
    }

    /**
     * Find credit card by id
     *
     * @param id
     * @return result
     */
    public CreditCardDto findCreditCardById(Long id) {
        CreditCard creditCard = creditCardEntityService.getByIdWithControl(id);
        CreditCardDto result = CreditCardMapper.INSTANCE.convertToCreditCardDto(creditCard);

        log.info("Find credit card by id success: {}", result.toString());
        return result;
    }

    /**
     * Get card details
     *
     * @param id
     * @return creditCardDetailsDto
     */
    public CreditCardDetailsDto getCardDetails(Long id) {
        CreditCard creditCard = creditCardEntityService.getByIdWithControl(id);
        LocalDateTime termEndDate = creditCard.getCutOffDate().atStartOfDay();
        Long creditCardId = creditCard.getId();
        LocalDateTime termStartDte = termEndDate.minusMonths(1);
        CreditCardDetailsDto creditCardDetailsDto = creditCardEntityService.getCreditCardDetails(creditCardId);

        List<CreditCardActivity> creditCardActivityList = creditCardActivityEntityService.findAllByCrdCreditCardIdAndTransactionDateBetween(creditCardId, termStartDte, termEndDate);
        List<CreditCardActivityDto> creditCardActivityDtoList = CreditCardMapper.INSTANCE.convertToCreditCardActivityDtoList(creditCardActivityList);
        creditCardDetailsDto.setCrdCreditCardActivityDtoList(creditCardActivityDtoList);

        log.info("Get card details successful: {}", creditCardDetailsDto.toString());
        return creditCardDetailsDto;
    }

    /**
     * This function used to save credit card
     *
     * @param creditCardSaveDto
     * @return creditCardDto
     */
    public CreditCardDto saveCreditCard(CreditCardSaveDto creditCardSaveDto) {
        BigDecimal earning = creditCardSaveDto.getEarning();
        Integer cutOffDay = creditCardSaveDto.getCutOffDay();

        creditCardValidationService.controlAreFieldsNull(earning, cutOffDay);
        creditCardValidationService.controlIsEarningNotNegative(earning);

        BigDecimal limit = earning.multiply(BigDecimal.valueOf(3));
        LocalDate cutOffDate = getCutOffDate(cutOffDay);
        CreditCard creditCard = createCreditCard(limit, cutOffDate);
        CreditCardDto creditCardDto = CreditCardMapper.INSTANCE.convertToCreditCardDto(creditCard);

        log.info("Save credit card successful: {}", creditCardDto.toString());
        return creditCardDto;
    }

    /**
     * This function used to get cut off date by using LocalDate to get year, month now
     *
     * @param cutOffDay
     * @return cutOffDate
     */
    public LocalDate getCutOffDate(Integer cutOffDay) {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        Month nextMonth = Month.of(currentMonth).plus(1);
        creditCardValidationService.isCutOffDayValid(cutOffDay);
        LocalDate cutOffDate = LocalDate.of(currentYear, nextMonth, cutOffDay);

        log.info("Get cut off date successful: {}", cutOffDate);
        return cutOffDate;
    }

    /**
     * This function used to create credit card and save to database.
     *
     * @param limit
     * @param cutOffDate
     * @return creditCard
     */
    public CreditCard createCreditCard(BigDecimal limit, LocalDate cutOffDate) {
        Long customerId = creditCardEntityService.getCurrentCustomerId();
        LocalDate expireDate = LocalDate.now().plusYears(3);
        LocalDate dueDate = cutOffDate.plusDays(10);
        Long cardNo = StringUtil.getRandomNumber(16);
        Long cvvNo = StringUtil.getRandomNumber(3);

        // Create credit card
        CreditCard creditCard = new CreditCard();
        creditCard.setCustomerId(customerId);
        creditCard.setDueDate(dueDate);
        creditCard.setExpireDate(expireDate);
        creditCard.setCardNo(cardNo);
        creditCard.setCvvNo(cvvNo);
        creditCard.setTotalLimit(limit);
        creditCard.setAvailableCardLimit(limit);
        creditCard.setMinimumPaymentAmount(BigDecimal.ZERO);
        creditCard.setCurrentDebit(BigDecimal.ZERO);
        creditCard.setCutOffDate(cutOffDate);
        creditCard.setStatusType(GenStatusType.ACTIVE);

        creditCard = creditCardEntityService.save(creditCard);

        log.info("Create credit card successful: {}", creditCard.toString());
        return creditCard;
    }

    public void cancelCreditCard(Long cardId) {
        CreditCard creditCard = creditCardEntityService.getByIdWithControl(cardId);
        creditCard.setStatusType(GenStatusType.PASSIVE);
        creditCard.setCancelDate(LocalDateTime.now());
        creditCardEntityService.save(creditCard);

    }

    /**
     * This function used to find credit card activity between date by using start date and end date
     *
     * @param creditCardId
     * @param startDate
     * @param endDate
     * @return creditCardActivityDtoList
     */
    public List<CreditCardActivityDto> findCreditCardActivityBetweenDates(Long creditCardId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        List<CreditCardActivity> creditCardActivityList = creditCardActivityEntityService.findCreditCardActivityBetweenDates(creditCardId, startDateTime, endDateTime);
        List<CreditCardActivityDto> creditCardActivityDtoList = CreditCardMapper.INSTANCE.convertToCreditCardActivityDtoList(creditCardActivityList);

        log.info("Find credit card activity between dates successful: {}", creditCardActivityDtoList.toString());
        return creditCardActivityDtoList;
    }

    /**
     * This function used to spend money
     *
     * @param creditCardSpendDto
     * @return creditCardActivityDto
     */
    public CreditCardActivityDto spendMoney(CreditCardSpendDto creditCardSpendDto) {

        creditCardValidationService.controlAreFieldsNull(creditCardSpendDto);
        BigDecimal amount = creditCardSpendDto.getAmount();
        String description = creditCardSpendDto.getDescription();

        CreditCard creditCard = getCreditCard(creditCardSpendDto);
        creditCardValidationService.controlIsCardCancelled(creditCard.getStatusType());
        creditCardValidationService.validateCreditCard(creditCard);
        BigDecimal currentDebit = creditCard.getCurrentDebit().add(amount);
        BigDecimal currentAvailableLimit = creditCard.getAvailableCardLimit().subtract(amount);

        creditCardValidationService.validateCardLimit(currentAvailableLimit);
        creditCard = updateCreditCardForSpend(creditCard, currentDebit, currentAvailableLimit);
        CreditCardActivity creditCardActivity = createCreditCardActivityForSpend(amount, description, creditCard);
        CreditCardActivityDto creditCardActivityDto = CreditCardMapper.INSTANCE.convertToCreditCardActivityDto(creditCardActivity);

        log.info("Spend money successful: {}", creditCardActivityDto.toString());
        return creditCardActivityDto;
    }

    /**
     * This function used to get credit card by cardNo, CVV and expireDate
     *
     * @param creditCardSpendDto
     * @return creditCard
     */
    private CreditCard getCreditCard(CreditCardSpendDto creditCardSpendDto) {
        Long cardNo = creditCardSpendDto.getCardNo();
        Long cvvNo = creditCardSpendDto.getCvvNo();
        LocalDate expireDate = creditCardSpendDto.getExpireDate();
        CreditCard creditCard = creditCardEntityService.findByCardNoAndCvvNoAndExpireDate(cardNo, cvvNo, expireDate);
        log.info(" Get credit card successful: {}", creditCard.toString());
        return creditCard;
    }

    private CreditCard updateCreditCardForSpend(CreditCard creditCard, BigDecimal currentDebit, BigDecimal currentAvailableLimit) {
        creditCard.setCurrentDebit(currentDebit);
        creditCard.setAvailableCardLimit(currentAvailableLimit);
        creditCard = creditCardEntityService.save(creditCard);
        return creditCard;

    }

    private CreditCardActivity createCreditCardActivityForSpend(BigDecimal amount, String description, CreditCard creditCard) {
        CreditCardActivity creditCardActivity = new CreditCardActivity();
        creditCardActivity.setCreditCardId(creditCard.getId());
        creditCardActivity.setAmount(amount);
        creditCardActivity.setDescription(description);
        creditCardActivity.setCardActivityType(CreditCardActivityType.SPEND);
        creditCardActivity.setTransactionDate(LocalDateTime.now());

        creditCardActivity = creditCardActivityEntityService.save(creditCardActivity);
        return creditCardActivity;
    }

    private CreditCardActivityDto refundMoney(Long activityId) {
        CreditCardActivity oldCreditCardActivity = creditCardActivityEntityService.getByIdWithControl(activityId);
        Long creditCardId = oldCreditCardActivity.getCreditCardId();
        CreditCard creditCard = creditCardEntityService.getByIdWithControl(creditCardId);
        creditCardValidationService.controlIsCardCancelled(creditCard.getStatusType());
        BigDecimal amount = oldCreditCardActivity.getAmount();
        creditCard = updateCreditCardForRefund(oldCreditCardActivity, amount);
        CreditCardActivity creditCardActivity = createCreditCardActivityForRefund(oldCreditCardActivity, amount, creditCard);
        CreditCardActivityDto result = CreditCardMapper.INSTANCE.convertToCreditCardActivityDto(creditCardActivity);
        return result;
    }

    private CreditCardActivity createCreditCardActivityForRefund(CreditCardActivity oldCreditCardActivity, BigDecimal amount, CreditCard creditCard) {
        String description = "REFUND :" + oldCreditCardActivity.getDescription();
        CreditCardActivity creditCardActivity = new CreditCardActivity();
        creditCardActivity.setCreditCardId(creditCard.getId());
        creditCardActivity.setAmount(amount);
        creditCardActivity.setDescription(description);
        creditCardActivity.setCardActivityType(CreditCardActivityType.REFUND);
        creditCardActivity.setTransactionDate(LocalDateTime.now());
        creditCardActivity = creditCardActivityEntityService.save(creditCardActivity);
        return creditCardActivity;
    }

    private CreditCard updateCreditCardForRefund(CreditCardActivity oldCreditCardActivity, BigDecimal amount) {
        CreditCard creditCard = creditCardEntityService.getByIdWithControl(oldCreditCardActivity.getCreditCardId());
        creditCard = addLimitToCard(creditCard, amount);
        return creditCard;
    }

    private CreditCard addLimitToCard(CreditCard creditCard, BigDecimal amount) {
        BigDecimal currentDebit = creditCard.getCurrentDebit().subtract(amount);
        BigDecimal currentAvailableLimit = creditCard.getAvailableCardLimit().add(amount);
        creditCard.setCurrentDebit(currentDebit);
        creditCard.setAvailableCardLimit(currentAvailableLimit);
        creditCard = creditCardEntityService.save(creditCard);
        return creditCard;
    }

    public CreditCardActivityDto receivePayment(CreditCardPaymentDto creditCardPaymentDto) {
        Long creditCardId = creditCardPaymentDto.getCreditCardId();
        BigDecimal amount = creditCardPaymentDto.getAmount();
        creditCardValidationService.controlAreFieldsNull(creditCardId, amount);

        CreditCard creditCard = creditCardEntityService.getByIdWithControl(creditCardId);
        creditCardValidationService.controlIsCardCancelled(creditCard.getStatusType());
        addLimitToCard(creditCard, amount);

        CreditCardActivity creditCardActivity = createCreditCardActivityForPayment(creditCardId, amount);

        CreditCardActivityDto creditCardActivityDto = CreditCardMapper.INSTANCE.convertToCreditCardActivityDto(creditCardActivity);
        return creditCardActivityDto;
    }

    private CreditCardActivity createCreditCardActivityForPayment(Long creditCardId, BigDecimal amount) {
        CreditCardActivity creditCardActivity = new CreditCardActivity();
        creditCardActivity.setCreditCardId(creditCardId);
        creditCardActivity.setAmount(amount);
        creditCardActivity.setDescription("PAYMENT: ");
        creditCardActivity.setCardActivityType(CreditCardActivityType.PAYMENT);
        creditCardActivity.setTransactionDate(LocalDateTime.now());

        creditCardActivity = creditCardActivityEntityService.save(creditCardActivity);
        return creditCardActivity;
    }

}
