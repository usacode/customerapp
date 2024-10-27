package com.mini.customerapi.service;

import com.mini.customerapi.dto.CustomerDto;
import com.mini.customerapi.exception.CustomerBadRequestException;
import com.mini.customerapi.exception.CustomerNotFoundException;
import com.mini.customerapi.exception.DuplicateEmailException;
import com.mini.customerapi.model.Customer;
import com.mini.customerapi.repository.CustomerRepository;
import com.mini.customerapi.util.CustomerMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * Service implementation for managing Customer entities.
 * This class provides methods for CRUD operations on Customer objects.
 */
@Service
public class CustomerServiceImpl implements ICustomerService {

    // Repository for performing database operations on Customer entities
    private final CustomerRepository customerRepository;

    /**
     * Constructor injection for the CustomerRepository.
     *
     * @param customerRepository Repository for Customer entity operations.
     */
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Saves a new Customer to the database.
     *
     * @param customerDto The Customer entity to save.
     * @return The saved Customer entity.
     */
    @Override
    public Customer saveCustomer(CustomerDto customerDto) {
        // Find the customer by Email and throw an exception if exist
        if (customerRepository.existsByEmailAddress(customerDto.getEmailAddress())) {
            throw new DuplicateEmailException(customerDto.getEmailAddress());
        }
        Customer customer = CustomerMapper.createCustomerMapper(customerDto);

        return customerRepository.save(customer);
    }



    /**
     * Retrieves all Customer entities from the database.
     *
     * @return A list of all Customer entities.
     */
    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves a specific Customer by their unique identifier.
     *
     * @param id The UUID of the Customer to retrieve.
     * @return the found Customer, or empty if not found.
     */
    @Override
    public Customer getCustomerById(UUID id) {
        // Find the customer by ID or throw an exception if not found
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));
    }

    /**
     * Deletes a Customer by their unique identifier.
     *
     * @param id The UUID of the Customer to delete.
     */
    @Override
    public void deleteCustomer(UUID id) {
        // Find the customer by ID and throw an exception if not exist
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id.toString());
        }
        customerRepository.deleteById(id);
    }

    /**
     * Update an existing Customer to the database.
     *
     * @param id The UUID of the Customer to update.
     * @param customerDto The Customer entity to save.
     * @return The updated Customer entity.
     */
    @Override
    public Customer updateCustomerById(UUID id, CustomerDto customerDto) {
        // Find the customer by ID or throw an exception if not found
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id.toString()));

        // Check if any other customer has the same email address
        boolean emailExists = StreamSupport.stream(customerRepository.findAll().spliterator(), false)
                .anyMatch(existingCustomer ->
                        existingCustomer.getEmailAddress().equalsIgnoreCase(customerDto.getEmailAddress())
                                && !existingCustomer.getId().equals(customer.getId())
                );

        if (emailExists) {
            throw new CustomerBadRequestException(id.toString()+" Repeating email");
        }

        // Update and save the customer
        return customerRepository.save(CustomerMapper.updateCustomerMapper(customerDto, customer));
    }


}
