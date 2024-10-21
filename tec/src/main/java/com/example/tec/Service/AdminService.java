package com.example.tec.Service;

import com.example.tec.model.User;
import com.example.tec.model.dtos.UserResponseDto;
import com.example.tec.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<ArrayList<UserResponseDto>> showUsers(){

        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();

        ArrayList<UserResponseDto> usersDtos = (ArrayList<UserResponseDto>) users.stream().map(user -> new UserResponseDto(user.getId(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAdmin())).collect(Collectors.toList());

        return ResponseEntity.ok(usersDtos);

    }

    public ResponseEntity<String> deleteUser(UUID id){

        userRepository.delete(userRepository.findById(id));

        return ResponseEntity.ok("user deleted.");

    }

    public ResponseEntity<String> changeRole(UUID id){

        User user = userRepository.findById(id);
        user.setAdmin(true);
        userRepository.save(user);

        return ResponseEntity.ok("user changed to admin.");

    }

}
