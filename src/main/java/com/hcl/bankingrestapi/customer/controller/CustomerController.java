package com.hcl.bankingrestapi.customer.controller;

import com.hcl.bankingrestapi.customer.dto.CustomerDto;
import com.hcl.bankingrestapi.customer.dto.CustomerSaveDto;
import com.hcl.bankingrestapi.customer.dto.CustomerUpdateDto;
import com.hcl.bankingrestapi.customer.service.CustomerService;
import com.hcl.bankingrestapi.general.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            tags = "Customer Controller",
            summary = "All Customers",
            description = "Gets all customers."
    )
    @GetMapping
    public ResponseEntity<RestResponse<List<CustomerDto>>> findAllCustomers() {
        List<CustomerDto> customerDtoList = customerService.findAllCustomers();
        return ResponseEntity.ok(RestResponse.of(customerDtoList));
    }

    @Operation(
            tags = "Customer Controller",
            summary = "Get a customer",
            description = "Gets a customer by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<CustomerDto>> findCustomerById(@PathVariable Long id) {
        CustomerDto customerDto = customerService.findCustomerById(id);
        return ResponseEntity.ok(RestResponse.of(customerDto));
    }

    @Operation(
            tags = "Customer Controller",
            summary = "Save a customer",
            description = "Save a new customer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = CustomerSaveDto.class
                                    ),
                                    examples = {
                                            @ExampleObject(
                                                    name = "new customer",
                                                    summary = "New Customer Example",
                                                    description = "Complete request with all available fields for customer",
                                                    value = "{\n" +
                                                            "  \"name\": \"string1\",\n" +
                                                            "  \"surname\": \"string1\",\n" +
                                                            "  \"identityNo\": 1,\n" +
                                                            "  \"password\": \"string1\"\n" +
                                                            "}"
                                            )
                                    }
                            ),
                    }
            )

    )
    @PostMapping("/save-customer")
    public ResponseEntity<RestResponse<MappingJacksonValue>> saveCustomer(@RequestBody CustomerSaveDto customerSaveDto) {
        CustomerDto customerDto = customerService.saveCustomer(customerSaveDto);
        WebMvcLinkBuilder linkGet = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        this.getClass()).findCustomerById(customerDto.getId()));
        WebMvcLinkBuilder linkDelete = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        this.getClass()).deleteCustomer(customerDto.getId()));
        EntityModel<CustomerDto> entityModel = EntityModel.of(customerDto);
        entityModel.add(linkGet.withRel("find-by-id"));
        entityModel.add(linkDelete.withRel("delete"));
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(entityModel);
        return ResponseEntity.ok(RestResponse.of(mappingJacksonValue));

    }

    @Operation(
            tags = "Customer Controller",
            summary = "Update a customer",
            description = "Updates customers all fields."
    )
    @PutMapping("/update-customer")
    public ResponseEntity<RestResponse<CustomerDto>> updateCustomer(@RequestBody CustomerUpdateDto customerUpdateDto) {
        CustomerDto customerDto = customerService.updateCustomer(customerUpdateDto);
        return ResponseEntity.ok(RestResponse.of(customerDto));
    }

    @Operation(
            tags = "Customer Controller",
            summary = "Delete a customer",
            description = "Delete a customer by id"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<?>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(RestResponse.empty());
    }
}
