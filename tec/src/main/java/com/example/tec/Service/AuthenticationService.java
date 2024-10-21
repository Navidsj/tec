package com.example.tec.Service;

import com.example.tec.checker.ValueCheck;
import com.example.tec.model.User;
import com.example.tec.model.dtos.LoginResponseDto;
import com.example.tec.model.dtos.LoginUserDto;
import com.example.tec.model.dtos.RegisterUserDto;
import com.example.tec.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<String> signup(RegisterUserDto input){
        if(userRepository.findByEmail(input.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("In email ghablan estefade shode ast!!!");
        }

        ResponseEntity<String> res = ValueCheck.checkUser(input.getName(),input.getEmail(),input.getPhoneNumber(),input.getPassword());

        if(res != null) return res;

        User user = new User();
        user.setName(input.getName());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEmail(input.getEmail());
        user.setPhoneNumber(input.getPhoneNumber());

        userRepository.save(user);

        return ResponseEntity.ok(user.getName() + " jan account shoma sakhte shod.");
    }

    public ResponseEntity<LoginResponseDto> authenticate(LoginUserDto input) throws JsonProcessingException {

        if(userRepository.findByEmail(input.getEmail()).isPresent() && passwordEncoder.matches(input.getPassword(), userRepository.findByEmail(input.getEmail()).get().getPassword())) {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
            User authonticatedUser = userRepository.findByEmail(input.getEmail()).orElseThrow();

            String jwtToken = jwtService.generateToken(authonticatedUser);

            LoginResponseDto loginResponse = new LoginResponseDto(jwtToken,jwtService.getJwtExpiration());

            return ResponseEntity.ok(loginResponse);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponseDto("Email or Password not correct!",0));
        }

    }


}
