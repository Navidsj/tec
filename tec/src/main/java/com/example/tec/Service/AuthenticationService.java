package com.example.tec.Service;

import com.example.tec.checker.ValueCheck;
import com.example.tec.mapper.Converter;
import com.example.tec.model.User;
import com.example.tec.model.dtos.LoginResponseDto;
import com.example.tec.model.dtos.LoginUserDto;
import com.example.tec.model.dtos.RegisterUserDto;
import com.example.tec.model.dtos.UserResponseDto;
import com.example.tec.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
   }

    public ResponseEntity<Object> signup(RegisterUserDto input){
        if(userRepository.findByEmail(input.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("In email ghablan estefade shode ast!!!");
        }

        RedissonClient redissonClient = Redisson.create();
        RList<Integer> captchaList = redissonClient.getList("captchaList");

        if(!captchaList.contains(input.getCaptcha())){
            return ResponseEntity.badRequest().body("captcha is not correct.");
        }

        for(int i=0;i < captchaList.size();i++){
            if(captchaList.get(i) == input.getCaptcha()) {
                captchaList.remove(i);
                break;
            }
        }

        ResponseEntity<Object> res = ValueCheck.checkUser(input.getName(),input.getEmail(),input.getPhoneNumber(),input.getPassword());

        if(res != null) return res;

        User user = new User();
        user.setName(input.getName());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());
        user.setPhoneNumber(input.getPhoneNumber());

        userRepository.save(user);

        ObjectMapper objectMapper = new ObjectMapper();

        UserResponseDto userDto = Converter.convertUserToUserDto(user);


        return ResponseEntity.ok(userDto);
    }

    public ResponseEntity<Object> authenticate(LoginUserDto input) throws JsonProcessingException {

        if(userRepository.findByEmail(input.getEmail()).isPresent() && passwordEncoder.matches(input.getPassword(), userRepository.findByEmail(input.getEmail()).get().getPassword())) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
            User authonticatedUser = userRepository.findByEmail(input.getEmail()).orElseThrow();

            String jwtToken = jwtService.generateToken(authonticatedUser);

            LoginResponseDto loginResponse = new LoginResponseDto(jwtToken,jwtService.getJwtExpiration());

            return ResponseEntity.ok(loginResponse);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email or Password not correct!");
        }

    }


}
