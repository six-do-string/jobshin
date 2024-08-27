package com.est.jobshin.domain.user.service;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.LoginRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;

import com.est.jobshin.global.security.service.UserDetailService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        userRepository.save(createUserRequest.toEntity());
    }

    /*@Transactional
    public void updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username:" + username));

        user.updateUser(updateUserRequest.getPassword(), updateUserRequest.getNickname());

        userRepository.save(user);
    }*/

    public UserResponse findByUsername(String username) {
        User user = userRepository.findByemail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username:" + username));

        return UserResponse.toDto(user);
    }

    public boolean isDuplicate(String username) {
        Optional<User> user = userRepository.findByemail(username);

        return user.isEmpty();
    }

}

