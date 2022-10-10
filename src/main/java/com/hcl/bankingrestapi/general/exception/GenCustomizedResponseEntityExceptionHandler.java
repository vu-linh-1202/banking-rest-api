package com.hcl.bankingrestapi.general.exception;

import com.hcl.bankingrestapi.general.dto.RestResponse;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
public class GenCustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This function use to handle all exceptions, return all errorDate, message and description
     *
     * @param e
     * @param webRequest
     * @return ResponseEntity
     */
    @ExceptionHandler
    public final ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest webRequest) {
        Date errorDate = new Date();
        String message = e.getMessage();
        String description = webRequest.getDescription(false);

        GenExceptionResponse genExceptionResponse = new GenExceptionResponse(errorDate, message, description);
        RestResponse<GenExceptionResponse> restResponse = RestResponse.error(genExceptionResponse);
        restResponse.setMessages(message);

        return new ResponseEntity<>(restResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * This function use to handle all item not found, return all error date, message and description for exception
     * @param itemNotFoundException
     * @return ResponseEntity
     */
    @ExceptionHandler
    public final ResponseEntity<Object> handleAllItemNotFoundException(ItemNotFoundException itemNotFoundException) {
        Date errorDate = new Date();
        String message = itemNotFoundException.getBaseErrorMessage().getMessage();
        String description = itemNotFoundException.getBaseErrorMessage().getDetailMessage();

        GenExceptionResponse genExceptionResponse = new GenExceptionResponse(errorDate, message, description);
        RestResponse<GenExceptionResponse> restResponse = RestResponse.error(genExceptionResponse);
        restResponse.setMessages(message);

        return new ResponseEntity<>(restResponse, HttpStatus.NOT_FOUND);
    }

}
