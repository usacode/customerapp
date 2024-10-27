package com.mini.customerapi.exception;

import java.util.UUID;

public class CustomerBadRequestException extends RuntimeException {
    public CustomerBadRequestException(String msg) {
        super("Request not correct with id: " + msg);
    }
}
