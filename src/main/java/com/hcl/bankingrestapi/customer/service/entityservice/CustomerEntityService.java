package com.hcl.bankingrestapi.customer.service.entityservice;

import com.hcl.bankingrestapi.customer.entity.Customer;
import com.hcl.bankingrestapi.customer.enums.CustomerErrorMessage;
import com.hcl.bankingrestapi.customer.repository.CustomerRepository;
import com.hcl.bankingrestapi.general.exception.ItemNotFoundException;
import com.hcl.bankingrestapi.general.service.BaseEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerEntityService extends BaseEntityService<Customer, CustomerRepository> {

    public CustomerEntityService(CustomerRepository dao) {
        super(dao);
    }

    public List<Customer> findAllCustomers() {
        List<Customer> customerList = getDao().findAll();
        return customerList;
    }

    public Customer saveCustomer(Customer customer) {
        customer = getDao().save(customer);
        return customer;
    }

    public Customer findByIdentityNo(Long identityNo) {
        Customer customer = getDao().findByIdentityNo(identityNo)
                .orElseThrow(() -> new ItemNotFoundException(CustomerErrorMessage.CUSTOMER_NOT_FOUND));
        return customer;
    }

    public Optional<Customer> findByIdentityNo(Customer customer) {
        Long identityNo = customer.getIdentityNo();
        Optional<Customer> customer1 = getDao().findByIdentityNo(identityNo);
        return customer1;
    }

    public Customer findCustomerById(Long id) {
        Customer customer = super.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(CustomerErrorMessage.CUSTOMER_NOT_FOUND));
        return customer;
    }
}
