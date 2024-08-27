package com.est.jobshin.domain.user.controller;

import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
		try {
			createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
			userService.createUser(createUserRequest);
			return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
		} catch (IllegalArgumentException e) {
			// 이메일 중복 시 예외 처리
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (Exception e) {
			// 기타 예외 처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
		}
	}
}
