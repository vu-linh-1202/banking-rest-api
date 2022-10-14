package com.hcl.bankingrestapi.customer.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class CustomerDto {
    Long id;
    private String name;
    private String surname;
    private String identityNo;

}
