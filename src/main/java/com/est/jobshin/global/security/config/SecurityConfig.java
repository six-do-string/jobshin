package com.est.jobshin.global.security.config;

import com.est.jobshin.global.security.handler.CustomAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/", "/user/**", "/user/login", "/user/signup")
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*",
                                        "/*/icon-*").permitAll()
                                .requestMatchers("/", "/user/login", "/user/signup", "/post/list")
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/user/login") // 로그인 페이지 URL
                        .loginProcessingUrl("/user/login") // 사용자 로그인 처리 URL
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 리다이렉트 URL
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/user/login") // 로그아웃 후 리다이렉트 URL
                )
                .authenticationProvider(authenticationProvider);
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}
