package com.example.tec.controller;


import com.example.tec.Service.AdminService;
import com.example.tec.Service.EditService;
import com.example.tec.Service.JwtService;
import com.example.tec.model.User;
import com.example.tec.model.dtos.UserDto;
import com.example.tec.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class AdminController {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AdminService adminService;
    private final EditService editService;

    public AdminController(UserRepository userRepository, JwtService jwtService, AdminService adminService, EditService editService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.adminService = adminService;
        this.editService = editService;
    }


    @GetMapping("/admin/show/users")
    public ResponseEntity<ArrayList<UserDto>> showUsers(@RequestHeader("Authorization") String header) throws Exception {
        String token = header.substring(7);
        User currentUser = userRepository.findByEmail(jwtService.extractUsername(token)).get();

        if(currentUser.getAdmin() == false){
            return (ResponseEntity<ArrayList<UserDto>>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }

        return adminService.showUsers();
    }

    @DeleteMapping("/admin/delete/user/{id}")
    public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String header, @PathVariable long id) throws Exception {
        String token = header.substring(7);
        User currentUser = userRepository.findByEmail(jwtService.extractUsername(token)).get();

        if(currentUser.getAdmin() == false){
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }

        return adminService.deleteUser(id);
    }

    @PutMapping("/admin/change/role/{id}")
    public ResponseEntity<String> changeRole(@RequestHeader("Authorization") String header,@PathVariable long id) throws Exception {
        String token = header.substring(7);
        User currentUser = userRepository.findByEmail(jwtService.extractUsername(token)).get();
        if(currentUser.getAdmin() == false){
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        return adminService.changeRole(id);
    }

    @PutMapping("/admin/edit/user/{id}")
    public ResponseEntity<String> editUser(@RequestHeader("Authorization") String header,@PathVariable long id,@RequestBody String body) throws Exception {
        String token = header.substring(7);
        User currentUser = userRepository.findByEmail(jwtService.extractUsername(token)).get();
        if(currentUser.getAdmin() == false){
            return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        return editService.editUser(id,body);
    }

}
