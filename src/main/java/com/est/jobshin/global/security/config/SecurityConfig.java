package com.est.jobshin.global.security.config;

import com.est.jobshin.global.security.handler.CustomAuthenticationFailureHandler;
import com.est.jobshin.global.security.service.UserDetailService;
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
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http,
            UserDetailService userDetailService) throws Exception {
        http
                .securityMatcher("/", "/views/**", "/views/users/login", "/views/users/signup",
                        "/views/edit", "/user/**", "/api/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*",
                                "/*/icon-*").permitAll()
                        .requestMatchers("/", "/views/users/login", "/views/users/signup",
                                "/api/users/signup", "/views/main", "/actuator/health")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/views/users/login")
                        .loginProcessingUrl("/views/users/login")  // 사용자 로그인 처리 URL
                        .defaultSuccessUrl("/", true)
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400)
                        .userDetailsService(userDetailService))
                .logout(logout -> logout
                        .logoutUrl("/api/users/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}
