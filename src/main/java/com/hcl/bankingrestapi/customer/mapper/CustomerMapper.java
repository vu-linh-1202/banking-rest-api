package com.hcl.bankingrestapi.customer.mapper;

import com.hcl.bankingrestapi.customer.dto.CustomerDto;
import com.hcl.bankingrestapi.customer.dto.CustomerSaveDto;
import com.hcl.bankingrestapi.customer.dto.CustomerUpdateDto;
import com.hcl.bankingrestapi.customer.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);


    List<CustomerDto> convertToCusCustomerDtoList(List<Customer> cusCustomerList);

    CustomerDto convertToCusCustomerDto(Customer cusCustomer);

    Customer convertToCusCustomer(CustomerSaveDto cusCustomerSaveDto);

    Customer convertToCusCustomer(CustomerUpdateDto cusCustomerUpdateDto);
}
