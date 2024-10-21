package com.example.tec.Service;

import com.example.tec.mapper.Converter;
import com.example.tec.model.User;
import com.example.tec.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class EditService {

    private final UserRepository userRepository;

    public EditService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> editUser(UUID userId, String body) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();



        Map<String,Object> edited = objectMapper.readValue(body,Map.class);
        Map<String,Object> current = objectMapper.convertValue(userRepository.findById(userId),Map.class);

        for (Map.Entry<String, Object> entry : edited.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equals("admin") || key.equals("userId") || key.equals("email"))
                continue;
            else{
                if(current.containsKey(key)){
                    current.put(key,value);
                }
            }
        }

        User newUser = Converter.convertMapToUser(current);
        newUser.setId(userId);
        userRepository.save(newUser);


        return ResponseEntity.ok("user edited.");
    }


}
