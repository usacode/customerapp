package com.mini.customerapi.controller;

import com.mini.customerapi.dto.CustomerDto;
import com.mini.customerapi.model.Customer;
import com.mini.customerapi.service.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {


    private final CustomerServiceImpl service;

    public CustomerController(CustomerServiceImpl service){
        this.service=service;
    }

    @Operation(summary = "Create a new customer", description = "Adds a new customer to the system")
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDto customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveCustomer(customer));
    }

    @Operation(summary = "Retrieve all customers", description = "Returns a list of all customers")
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllCustomers());

    }

    @Operation(summary = "Find a customer by ID", description = "Returns a single customer by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @Parameter(description = "ID of the customer to retrieve", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getCustomerById(id));

    }
    @Operation(summary = "Update a customer by ID", description = "Returns a single updated customer")
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomerById(@RequestBody CustomerDto customer,
            @Parameter(description = "ID of the customer to update", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.updateCustomerById(id,customer));

    }

    @Operation(summary = "Delete a customer by ID", description = "Removes a customer from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to delete", required = true)
            @PathVariable UUID id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
