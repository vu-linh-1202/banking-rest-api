package com.hcl.bankingrestapi.customer.service;

import com.hcl.bankingrestapi.customer.entity.Customer;
import com.hcl.bankingrestapi.customer.enums.CustomerErrorMessage;
import com.hcl.bankingrestapi.customer.service.entityservice.CustomerEntityService;
import com.hcl.bankingrestapi.general.exception.IllegalFieldException;
import com.hcl.bankingrestapi.general.exception.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerValidationService {

    private final CustomerEntityService customerEntityService;

    public void controlIsCustomerExist(Long id) {
        boolean isExist = customerEntityService.existById(id);
        if (!isExist) {
            throw new ItemNotFoundException(CustomerErrorMessage.CUSTOMER_NOT_FOUND);
        }
    }

    public void controlAreFieldsNonNull(Customer customer) {
        boolean hasNullField =
                customer.getName().isBlank() ||
                        customer.getSurname().isBlank() ||
                        customer.getIdentityNo() == null ||
                        customer.getPassword().isBlank();
        if (hasNullField) {
            throw new IllegalFieldException(CustomerErrorMessage.FIELD_CANNOT_BE_NULL);
        }
    }

    public void controlIsIdentityNoUnique(Customer customer) {
        Optional<Customer> customerOptional = customerEntityService.findByIdentityNo(customer);
        Customer customerReturned;
        if (customerOptional.isPresent()) {
            customerReturned = customerOptional.get();
            boolean didMatchedItSelf = didMatchedItself(customerReturned, customer);
            if (!didMatchedItSelf) {
                throw new IllegalFieldException(CustomerErrorMessage.IDENTITY_NO_MUST_BE_UNIQUE);
            }
        }

    }

    private Boolean didMatchedItself(Customer usrUserReturned, Customer cusCustomer) {

        return usrUserReturned.getId().equals(cusCustomer.getId());
    }
}
