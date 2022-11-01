package com.hcl.bankingrestapi.creditcard.enums;

import com.hcl.bankingrestapi.general.enums.BaseErrorMessage;

public enum CreditCardErrorMessage implements BaseErrorMessage {

    PARAMETER_MIN_CANNOT_BE_LARGER_THAN_MAX("Parameter min cannot be larger than max", "Please be sure that parameter min is not larger than max"),

    CUT_OFF_DAY_IS_NOT_VALID("Cut off day is not valid", "Please be sure that the cut off day id between [1,31]."),

    INSUFFICIENT_CREDIT_CARD_LIMIT("Insufficient credit card limit", "You have reached your card's limit"),

    INVALID_CREDIT_CARD("Invalid credit card.", "Credit card is not found"),

    CREDIT_CARD_EXPIRED("Credit card expired", "Please check your credit cards expire date"),

    EARNING_CANNOT_BE_NEGATIVE("Earning cannot be negative", "Please check that your earning is not negative"),

    CREDIT_CARD_NOT_FOUND("Credit card not found", "Please be sure that your credit card is belongs to a credit card"),

    FIELDS_CANNOT_BE_NEGATIVE("Fields cannot be negative", "Please be sure that you entered all the fields"),

    CREDIT_CARD_CANCELLED("Credit card cancelled", "Please be sure that your card is not cancelled or you entered the correct card credentials"),
    ;

    private final String message;
    private final String detailMessage;

    CreditCardErrorMessage(String message, String detailMessage) {
        this.message = message;
        this.detailMessage = detailMessage;
    }

    @Override
    public String getDetailMessage() {
        return detailMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
