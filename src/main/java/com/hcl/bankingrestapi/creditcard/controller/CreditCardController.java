package com.hcl.bankingrestapi.creditcard.controller;

import com.hcl.bankingrestapi.creditcard.dto.*;
import com.hcl.bankingrestapi.creditcard.service.CreditCardActivityService;
import com.hcl.bankingrestapi.creditcard.service.CreditCardService;
import com.hcl.bankingrestapi.general.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.swing.event.ListDataEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;
    private final CreditCardActivityService creditCardActivityService;

    @Operation(
            tags = "Credit Card Controller",
            summary = "All credit card",
            description = "Get all active credit card."
    )
    @GetMapping
    public ResponseEntity<RestResponse<List<CreditCardDto>>> findAllCreditCards() {
        List<CreditCardDto> creditCardDtoList = creditCardService.findAllCreditCards();
        return ResponseEntity.ok(RestResponse.of(creditCardDtoList));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Get a credit card",
            description = "Gets a credit card by id."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<CreditCardDto>> findCreditCardById(@PathVariable Long id) {
        CreditCardDto creditCardDto = creditCardService.findCreditCardById(id);
        return ResponseEntity.ok(RestResponse.of(creditCardDto));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Get credit card activity by price interval",
            description = "Get credit card activity in the range by given min and max"
    )
    @GetMapping("/find-activity-by-amount-interval")
    public ResponseEntity<RestResponse<List<CreditCardActivityDto>>> findCreditCardActivityByAmountInterval(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        List<CreditCardActivityDto> creditCardActivityDtoList = creditCardActivityService.findCreditCardActivityByAmountInterval(min, max);
        return ResponseEntity.ok(RestResponse.of(creditCardActivityDtoList));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Get a credit card activities betwwen dates",
            description = "Date format: YYYY-MM-DD. Gets a credit card activities between dates pageable"
    )
    @GetMapping("/{creditCardId}/activities")
    public ResponseEntity<RestResponse<List<CreditCardActivityDto>>> findCreditCardBetweenDates(@PathVariable Long creditCardId,
                                                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CreditCardActivityDto> creditCardActivityDtoList =
                creditCardService.findCreditCardActivityBetweenDates(creditCardId, startDate, endDate);
        return ResponseEntity.ok(RestResponse.of(creditCardActivityDtoList));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Get an analysis about credit card activities.",
            description = "Gets an analysis about credit card activity's minimum, maximum, and average amounts, " +
                    "count of credit card activities and credit card activity by credit card activity type."
    )
    @GetMapping("/get-card-activity-analysis/{creditCardId}")
    public ResponseEntity<RestResponse<List<CreditCardActivityAnalysisDto>>> getCardActivityAnalysis(@PathVariable Long creditCardId) {

        List<CreditCardActivityAnalysisDto> creditCardActivityAnalysisDtoList = creditCardActivityService.getCardActivityAnalysis(creditCardId);

        return ResponseEntity.ok(RestResponse.of(creditCardActivityAnalysisDtoList));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Get credit card details",
            description = "Get details of your credit card."
    )
    @GetMapping("/{id}/cardDetails")
    public ResponseEntity<RestResponse<CreditCardDetailsDto>> getCardDetails(@PathVariable Long id) {

        CreditCardDetailsDto crdCreditCardDetailsDto = creditCardService.getCardDetails(id);

        return ResponseEntity.ok(RestResponse.of(crdCreditCardDetailsDto));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Save a credit card",
            description = "Save a credit card."
    )
    @PostMapping("/save-credit-card")
    public ResponseEntity<RestResponse<MappingJacksonValue>> saveCreditCard(@RequestBody CreditCardSaveDto crdCreditCardSaveDto) {

        CreditCardDto creditCardDto = creditCardService.saveCreditCard(crdCreditCardSaveDto);

        WebMvcLinkBuilder linkGet = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        this.getClass()).findCreditCardById(creditCardDto.getId()));

        WebMvcLinkBuilder linkDelete = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        this.getClass()).cancelCreditCard(creditCardDto.getId()));

        EntityModel<CreditCardDto> entityModel = EntityModel.of(creditCardDto);

        entityModel.add(linkGet.withRel("find-by-id"));
        entityModel.add(linkDelete.withRel("cancel"));

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(entityModel);

        return ResponseEntity.ok(RestResponse.of(mappingJacksonValue));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Spend money from credit card.",
            description = "Spend money from credit card."
    )
    @PostMapping("/spend-money")
    public ResponseEntity<RestResponse<CreditCardActivityDto>> spendMoney(@RequestBody CreditCardSpendDto crdCreditCardSpendDto) {

        CreditCardActivityDto creditCardActivityDto = creditCardService.spendMoney(crdCreditCardSpendDto);

        return ResponseEntity.ok(RestResponse.of(creditCardActivityDto));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Refund money from credit card.",
            description = "Refund money from credit card."
    )
    @PostMapping("/refund/{activityId}")
    public ResponseEntity<RestResponse<CreditCardActivityDto>> refundMoney(@PathVariable Long activityId) {

        CreditCardActivityDto creditCardActivityDto = creditCardService.refundMoney(activityId);

        return ResponseEntity.ok(RestResponse.of(creditCardActivityDto));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Get payment money.",
            description = "Get payment money (card limit increase)."
    )
    @PostMapping("/receive-payment")
    public ResponseEntity<RestResponse<CreditCardActivityDto>> receivePayment(@RequestBody CreditCardPaymentDto crdCreditCardPaymentDto) {

        CreditCardActivityDto creditCardActivityDto = creditCardService.receivePayment(crdCreditCardPaymentDto);

        return ResponseEntity.ok(RestResponse.of(creditCardActivityDto));
    }

    @Operation(
            tags = "Credit Card Controller",
            summary = "Cancel a credit card",
            description = "Cancel a credit card by making its status passive."
    )
    @PatchMapping("/{cardId}")
    public ResponseEntity<RestResponse<?>> cancelCreditCard(@PathVariable Long cardId) {

        creditCardService.cancelCreditCard(cardId);

        return ResponseEntity.ok(RestResponse.empty());
    }
}
