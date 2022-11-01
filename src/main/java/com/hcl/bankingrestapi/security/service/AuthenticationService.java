package com.hcl.bankingrestapi.security.service;

import com.hcl.bankingrestapi.customer.dto.CustomerDto;
import com.hcl.bankingrestapi.customer.dto.CustomerSaveDto;
import com.hcl.bankingrestapi.customer.entity.Customer;
import com.hcl.bankingrestapi.customer.service.CustomerService;
import com.hcl.bankingrestapi.customer.service.entityservice.CustomerEntityService;
import com.hcl.bankingrestapi.security.dto.SecurityLoginRequestDto;
import com.hcl.bankingrestapi.security.enums.EnumJwtConstant;
import com.hcl.bankingrestapi.security.securitys.JwtTokenGenerator;
import com.hcl.bankingrestapi.security.securitys.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final CustomerService customerService;
    private final CustomerEntityService customerEntityService;
    private AuthenticationManager authenticationManager;
    private final JwtTokenGenerator jwtTokenGenerator;

    /**
     *
     * @param customerSaveDto
     * @return customerDto
     */
    public CustomerDto register(CustomerSaveDto customerSaveDto) {
        CustomerDto customerDto = customerService.saveCustomer(customerSaveDto);
        return customerDto;
    }

    /**
     *
     * @param securityLoginRequestDto
     * @return bearer + token
     */
    public String login(SecurityLoginRequestDto securityLoginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                securityLoginRequestDto.getIdentityNo().toString(),
                securityLoginRequestDto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenGenerator.generateJwtToken(authentication);
        String bearer = EnumJwtConstant.BEARER.getConstant();
        return bearer + token;
    }

    public Customer getCurrentCustomer() {
        JwtUserDetails jwtUserDetails = getCurrentJwtUserDetails();

        Customer customer = null;
        if (jwtUserDetails != null) {
            customer = customerEntityService.getByIdWithControl(jwtUserDetails.getId());
        }
        return customer;
    }

    public Long getCurrentCustomerId() {
        JwtUserDetails jwtUserDetails = getCurrentJwtUserDetails();
        Long jwtUserDetailsId = null;
        if (jwtUserDetails != null) {
            jwtUserDetailsId = jwtUserDetails.getId();
        }
        return jwtUserDetailsId;
    }

    private JwtUserDetails getCurrentJwtUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUserDetails jwtUserDetails = null;
        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        }
        return jwtUserDetails;
    }
}
