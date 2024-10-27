package com.mini.customerapi.repository;

import com.mini.customerapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // Custom method to check for existing email address
    boolean existsByEmailAddress(String emailAddress);
}