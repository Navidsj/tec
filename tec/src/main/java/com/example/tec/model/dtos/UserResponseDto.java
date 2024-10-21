package com.example.tec.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserResponseDto {


    UUID Id;
    String name;
    String email;
    String phoneNumber;
    Boolean admin;

}
