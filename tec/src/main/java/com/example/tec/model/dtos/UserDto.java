package com.example.tec.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {


    long Id;
    String name;
    String email;
    String phoneNumber;
    Boolean admin;

}
