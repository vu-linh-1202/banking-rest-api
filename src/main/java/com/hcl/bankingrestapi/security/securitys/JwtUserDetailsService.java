package com.hcl.bankingrestapi.security.securitys;

import com.hcl.bankingrestapi.customer.entity.Customer;
import com.hcl.bankingrestapi.customer.service.CustomerService;
import com.hcl.bankingrestapi.customer.service.entityservice.CustomerEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final CustomerEntityService customerEntityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long identityNo = Long.valueOf(username);
        Customer customer = customerEntityService.findByIdentityNo(identityNo);
        return JwtUserDetails.create(customer);
    }

    public UserDetails loadUserByUserId(Long id) {
        Customer customer = customerEntityService.getByIdWithControl(id);
        return JwtUserDetails.create(customer);
    }
}
