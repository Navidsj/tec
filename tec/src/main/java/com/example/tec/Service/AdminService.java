package com.example.tec.Service;

import com.example.tec.model.User;
import com.example.tec.model.dtos.UserDto;
import com.example.tec.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<ArrayList<UserDto>> showUsers(){

        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();

        ArrayList<UserDto> usersDtos = (ArrayList<UserDto>) users.stream().map(user -> new UserDto(user.getId(),user.getName(),user.getEmail(),user.getPhoneNumber(),user.getAdmin())).collect(Collectors.toList());

        return ResponseEntity.ok(usersDtos);

    }

    public ResponseEntity<String> deleteUser(long id){

        userRepository.delete(userRepository.findById(id).get());

        return ResponseEntity.ok("user deleted.");

    }

    public ResponseEntity<String> changeRole(long id){

        User user = userRepository.findById(id).get();
        user.setAdmin(true);
        userRepository.save(user);

        return ResponseEntity.ok("user changed to admin.");

    }

}
