package com.mini.customerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.customerapi.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper = new ObjectMapper();

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setFirstName("John");
        customer.setMiddleName("Doe");
        customer.setLastName("Smith");
        customer.setEmailAddress("john.doe+" + UUID.randomUUID() + "@example.com"); // Unique email
        customer.setPhoneNumber("123-456-7890");
    }

    @Test
    public void testCreateCustomer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.emailAddress", is(customer.getEmailAddress())));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetCustomerById() throws Exception {
        // Save a customer to get a known ID for testing
        String savedCustomerResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Customer savedCustomer = objectMapper.readValue(savedCustomerResponse, Customer.class);
        UUID customerId = savedCustomer.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerId.toString())))
                .andExpect(jsonPath("$.firstName", is(customer.getFirstName())));
    }
    @Test
    public void testUpdateCustomerById() throws Exception {
        // Save a customer to get a known ID for testing
        String savedCustomerResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Customer savedCustomer = objectMapper.readValue(savedCustomerResponse, Customer.class);
        UUID customerId = savedCustomer.getId();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(customerId.toString())))
                .andExpect(jsonPath("$.firstName", is(customer.getFirstName())));
    }


    @Test
    public void testDeleteCustomer() throws Exception {
        // Save a customer to get a known ID for testing
        String savedCustomerResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Customer savedCustomer = objectMapper.readValue(savedCustomerResponse, Customer.class);
        UUID customerId = savedCustomer.getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Confirm deletion
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
