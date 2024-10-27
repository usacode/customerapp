package com.mini.customerapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private String firstName;

    private String middleName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;


}
