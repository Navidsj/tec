package com.example.tec.controller;


import com.example.tec.Service.EditService;
import com.example.tec.Service.JwtService;
import com.example.tec.model.User;
import com.example.tec.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.image.BufferedImage;

@RestController
public class EditController {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EditService editService;

    public EditController(UserRepository userRepository, JwtService jwtService, EditService editService, EditService editService1) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.editService = editService1;
    }

    @PutMapping("/edit")
    public ResponseEntity<String> edit(@RequestHeader("Authorization") String header, @RequestBody String body) throws Exception {

        String token = header.substring(7);
        User currentUser = userRepository.findByEmail(jwtService.extractUsername(token)).get();

        return editService.editUser(currentUser.getId(),body);
    }


}
