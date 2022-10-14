package com.hcl.bankingrestapi.customer.repository;

import com.hcl.bankingrestapi.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByIdentityNo(Long identityNo);
}
