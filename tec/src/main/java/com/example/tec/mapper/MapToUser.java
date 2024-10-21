package com.example.tec.mapper;

import com.example.tec.model.User;

import java.util.Map;

public class MapToUser {

    public static User convertMapToUser(Map<String,Object> map){
        User newUser = new User();
        newUser.setName((String) map.get("name"));
        newUser.setId((Long) map.get("id"));
        newUser.setEmail((String) map.get("email"));
        newUser.setPassword((String) map.get("password"));
        newUser.setPhoneNumber((String) map.get("phoneNumber"));
        newUser.setAdmin((Boolean) map.get("admin"));
        return newUser;
    }

}
