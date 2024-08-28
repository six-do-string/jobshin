package com.est.jobshin.global.security.service;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.global.security.model.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));

        return new CustomUserDetails(UserResponse.toDto(user), authorities);
    }
}