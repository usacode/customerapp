package com.mini.customerapi.exception;

import java.util.UUID;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String msg) {
        super("Customer not found with id: " + msg);
    }
}
