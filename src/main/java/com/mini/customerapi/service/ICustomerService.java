package com.mini.customerapi.service;

import com.mini.customerapi.dto.CustomerDto;
import com.mini.customerapi.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICustomerService {

    Customer saveCustomer(CustomerDto customerDto);

    List<Customer> getAllCustomers();

    Customer getCustomerById(UUID id);

    void deleteCustomer(UUID id);

    Customer updateCustomerById(UUID id,CustomerDto customer);
}
