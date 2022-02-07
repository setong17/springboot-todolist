package com.springboot.todolist.service;

import com.springboot.todolist.model.UserEntity;
import com.springboot.todolist.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {

        if( userEntity == null || userEntity.getEmail() == null ) {
            throw new RuntimeException("Invalid arguments");
        }

        final String email = userEntity.getEmail();

        if( userRepository.existsByEmail(email) ) {
            log.warn("Email is already exists {}, email");
            throw new RuntimeException("Email is already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(
            final String email
            , final String password
            , final PasswordEncoder encoder) {

        final UserEntity originalUser = userRepository.findByEmail(emal);

        // mathes 메서드를 이용해 패스워드가 같은지 확인
        if( originalUser != nulll
                && encoder.matches(password, originalUser.getPassword()) ) {

            return originalUser;
        }

        return null;
    }

}
