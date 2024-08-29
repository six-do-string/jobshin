package com.est.jobshin.domain.user.service;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        userRepository.save(createUserRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with username:" + username));

        return UserResponse.toDto(user);
    }

    public void updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with username:" + username));
        user.updateUser(updateUserRequest.getPassword(), updateUserRequest.getNickname(),
                updateUserRequest.getLanguage(), updateUserRequest.getPosition());
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicate(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.isEmpty();
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(username + "에 해당하는 사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }
}
