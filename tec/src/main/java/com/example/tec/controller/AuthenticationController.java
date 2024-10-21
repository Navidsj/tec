package com.example.tec.controller;

import com.example.tec.Service.AuthenticationService;
import com.example.tec.Service.JwtService;
import com.example.tec.model.dtos.LoginResponseDto;
import com.example.tec.model.dtos.LoginUserDto;
import com.example.tec.model.dtos.RegisterUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final ObjectMapper jacksonObjectMapper;


    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, ObjectMapper jacksonObjectMapper) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> register(@RequestBody RegisterUserDto registerUserDto) throws InterruptedException, JsonProcessingException, BadRequestException {
        return authenticationService.signup(registerUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginUserDto loginUserDto) throws JsonProcessingException {
        return authenticationService.authenticate(loginUserDto);
    }



}