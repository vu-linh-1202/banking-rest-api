package com.hcl.bankingrestapi.general.enums;

import org.apache.kafka.common.protocol.types.Field;

public enum GenErrorMessage implements BaseErrorMessage{
    ITEM_NOT_FOUND("Item not found!", "Please check the id of the item."),
    VALUE_CANNOT_BE_NEGATIVE("Value cannot be negative!","Please enter a value that is zero or larger"),
    PARAMETER_CANNOT_BE_NULL("Parameter cannot be null","Please enter a parameter"),
    ;

    private final String message;
    private final String detailMessage;

    GenErrorMessage(String message, String detailMessage){
        this.message=message;
        this.detailMessage=detailMessage;
    }
    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDetailMessage() {
        return detailMessage;
    }
}
