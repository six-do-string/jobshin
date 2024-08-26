package com.est.jobshin.domain.user.service;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.repository.UserRepository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

    @Service
    @RequiredArgsConstructor
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public void createUser(CreateUserRequest createUserRequest) {
            userRepository.save(createUserRequest.toEntity());
        }

        @Transactional(readOnly = true)
        public boolean isDuplicate(String email) {
            Optional<User> user = userRepository.findByemail(email);

            return user.isEmpty();
        }
    }
