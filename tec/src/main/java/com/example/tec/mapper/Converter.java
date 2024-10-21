package com.example.tec.mapper;

import com.example.tec.model.User;
import com.example.tec.model.dtos.UserResponseDto;

import java.util.Map;
import java.util.UUID;

public class Converter {

    public static User convertMapToUser(Map<String,Object> map){
        User newUser = new User();
        newUser.setName((String) map.get("name"));
        newUser.setEmail((String) map.get("email"));
        newUser.setPassword((String) map.get("password"));
        newUser.setPhoneNumber((String) map.get("phoneNumber"));
        newUser.setAdmin((Boolean) map.get("admin"));
        return newUser;
    }

    public static UserResponseDto convertUserToUserDto(User user){
        UserResponseDto userDto = new UserResponseDto(user.getId(), user.getName(), user.getEmail(),user.getPhoneNumber(),user.getAdmin());
        return userDto;
    }


}
