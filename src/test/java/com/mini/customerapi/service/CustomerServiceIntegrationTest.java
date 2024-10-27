package com.mini.customerapi.service;


import com.mini.customerapi.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/api/customers";
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmailAddress("john.doe@example.com");
        customer.setPhoneNumber("123-456-7890");

        ResponseEntity<Customer> response = restTemplate.postForEntity(baseUrl, customer, Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmailAddress()).isEqualTo("john.doe@example.com");
    }

    @Test
    public void testGetAllCustomers() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isInstanceOf(List.class);
    }

    @Test
    public void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmailAddress("jane.doe@example.com");
        customer.setPhoneNumber("123-456-7891");

        // First, create the customer
        Customer createdCustomer = restTemplate.postForObject(baseUrl, customer, Customer.class);

        // Then, retrieve the customer by ID
        ResponseEntity<Customer> response = restTemplate.getForEntity(baseUrl + "/" + createdCustomer.getId(), Customer.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdCustomer.getId());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Alice");
        customer.setLastName("Smith");
        customer.setEmailAddress("alice.smith@example.com");
        customer.setPhoneNumber("123-456-7892");

        // First, create the customer
        Customer createdCustomer = restTemplate.postForObject(baseUrl, customer, Customer.class);

        // Then, delete the customer
        restTemplate.delete(baseUrl + "/" + createdCustomer.getId());

        // Attempt to retrieve the deleted customer
        ResponseEntity<Customer> response = restTemplate.getForEntity(baseUrl + "/" + createdCustomer.getId(), Customer.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
