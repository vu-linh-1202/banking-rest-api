package com.hcl.bankingrestapi.security.controller;

import com.hcl.bankingrestapi.customer.dto.CustomerDto;
import com.hcl.bankingrestapi.customer.dto.CustomerSaveDto;
import com.hcl.bankingrestapi.general.dto.RestResponse;
import com.hcl.bankingrestapi.security.dto.SecurityLoginRequestDto;
import com.hcl.bankingrestapi.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(tags = "Authentication Controller")
    @PostMapping("/login")
    public ResponseEntity<RestResponse<String>> login(@RequestBody SecurityLoginRequestDto securityLoginRequestDto) {
        String token = authenticationService.login(securityLoginRequestDto);
        return ResponseEntity.ok(RestResponse.of(token));
    }

    @Operation(tags = "Authentication Controller")
    @PostMapping("/register")
    public ResponseEntity<RestResponse<CustomerDto>> register(@RequestBody CustomerSaveDto customerSaveDto) {
        CustomerDto customerDto = authenticationService.register(customerSaveDto);
        return ResponseEntity.ok(RestResponse.of(customerDto));
    }

}
