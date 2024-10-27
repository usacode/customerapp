package com.mini.customerapi.util;

import com.mini.customerapi.dto.CustomerDto;
import com.mini.customerapi.model.Customer;

public class CustomerMapper {

    private CustomerMapper (){}

    public static Customer createCustomerMapper(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setMiddleName(customerDto.getMiddleName());
        customer.setEmailAddress(customerDto.getEmailAddress());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        return customer;
    }

    public static Customer updateCustomerMapper(CustomerDto customerDto, Customer customer) {
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setEmailAddress(customerDto.getEmailAddress());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setMiddleName(customerDto.getMiddleName());
        return customer;
    }
}
