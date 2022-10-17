package com.hcl.bankingrestapi.customer.service;

import com.hcl.bankingrestapi.customer.dto.CustomerDto;
import com.hcl.bankingrestapi.customer.dto.CustomerSaveDto;
import com.hcl.bankingrestapi.customer.dto.CustomerUpdateDto;
import com.hcl.bankingrestapi.customer.entity.Customer;
import com.hcl.bankingrestapi.customer.mapper.CustomerMapper;
import com.hcl.bankingrestapi.customer.service.entityservice.CustomerEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    public final CustomerEntityService customerEntityService;
    public final CustomerValidationService customerValidationService;
    private final PasswordEncoder passwordEncoder;

    public List<CustomerDto> findAllCustomers() {
        List<Customer> customerList = customerEntityService.findAllCustomers();
        List<CustomerDto> customerDtoList = CustomerMapper.INSTANCE.convertToCusCustomerDtoList(customerList);
        return customerDtoList;
    }

    public CustomerDto findCustomerById(Long id) {
        Customer customer = customerEntityService.getByIdWithControl(id);
        CustomerDto customerDto = CustomerMapper.INSTANCE.convertToCusCustomerDto(customer);
        return customerDto;
    }

    /**
     *
     * @param customerSaveDto
     * @return customerDto
     */
    public CustomerDto saveCustomer(CustomerSaveDto customerSaveDto) {
        Customer customer = CustomerMapper.INSTANCE.convertToCusCustomer(customerSaveDto);
        String password = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(password);

        customerValidationService.controlAreFieldsNonNull(customer);
        customerValidationService.controlIsIdentityNoUnique(customer);

        customer = customerEntityService.saveCustomer(customer);

        CustomerDto customerDto = CustomerMapper.INSTANCE.convertToCusCustomerDto(customer);
        return customerDto;
    }

    /**
     *
     * @param customerUpdateDto
     * @return
     */
    public CustomerDto updateCustomer(CustomerUpdateDto customerUpdateDto){
        Long id = customerUpdateDto.getId();
        customerValidationService.controlIsCustomerExist(id);

        Customer customer= CustomerMapper.INSTANCE.convertToCusCustomer(customerUpdateDto);
        customerValidationService.controlAreFieldsNonNull(customer);
        customerValidationService.controlIsIdentityNoUnique(customer);

        String password= customerEntityService.findCustomerById(id).getPassword();
        boolean passwordIsSame= customer.getPassword().equals(password);
        if (!passwordIsSame){
            String newPassword= passwordEncoder.encode(customer.getPassword());
            customer.setPassword(newPassword);
        }
        customer=customerEntityService.saveCustomer(customer);
        CustomerDto customerDto= CustomerMapper.INSTANCE.convertToCusCustomerDto(customer);
        return customerDto;
    }

    /**
     *
     * @param id
     */
    public void deleteCustomer(Long id){
        Customer customer= customerEntityService.getByIdWithControl(id);
        customerEntityService.delete(customer);
    }
}
