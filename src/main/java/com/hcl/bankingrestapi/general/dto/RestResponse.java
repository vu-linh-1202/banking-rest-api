package com.hcl.bankingrestapi.general.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> implements Serializable {
    private T data;
    private Date responseDate;
    private boolean isSuccess;
    private String messages;

    public RestResponse(T data, boolean isSuccess) {
        this.data = data;
        this.isSuccess = isSuccess;
        responseDate = new Date();
    }

    public static <T> RestResponse<T> of(T t) {
        return new RestResponse<>(t, true);
    }

    public static <T> RestResponse<T> error(T t) {
        return new RestResponse<>(t, false);
    }

    public static <T> RestResponse<T> empty(T t) {
        return new RestResponse<>(t, false);
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getMessages() {
        return messages;
    }
}

