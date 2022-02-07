package com.springboot.todolist.controller;

import com.springboot.todolist.dto.ResponseDTO;
import com.springboot.todolist.dto.UserDTO;
import com.springboot.todolist.model.UserEntity;
import com.springboot.todolist.security.TokenProvider;
import com.springboot.todolist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    // Bean으로 작성해도 됨
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {

        try {

            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username((userDTO.getUsername()))
                    .password(userDTO.getPassword())
                    .build();

            UserEntity registerdUser = userService.create(user);

            UserDTO responseDTO = UserDTO.builder()
                    .email(registerdUser.getEmail())
                    .id(registerdUser.getId())
                    .username(registerdUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseDTO);

        } catch (Exception e) {

            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }

    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {

        UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);

        if( user != null ) {

            final String token = tokenProvider.create(user);

            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);

        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }




}
