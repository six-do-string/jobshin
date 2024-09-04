package com.est.jobshin.controller;

import com.est.jobshin.domain.interview.dto.InterviewHistorySummaryResponse;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.controller.UserController;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.MyPageInterviewWithDetailsDto;
import com.est.jobshin.domain.user.dto.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Position;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class UserControllerTest {

    // Mock 객체 정의
    @Mock
    private UserService userService; // UserService를 Mock 객체로 정의하여 실제 로직을 대체

    @Mock
    private PasswordEncoder passwordEncoder; // PasswordEncoder를 Mock 객체로 정의하여 비밀번호 암호화 로직을 대체

    @Mock
    private BindingResult bindingResult; // BindingResult를 Mock 객체로 정의하여 유효성 검사 결과를 테스트

    @Mock
    private HttpServletRequest request; // HttpServletRequest를 Mock 객체로 정의하여 요청 객체를 대체

    @Mock
    private HttpServletResponse response; // HttpServletResponse를 Mock 객체로 정의하여 응답 객체를 대체

    // UserController에 위의 Mock 객체들을 주입
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    // 각 테스트 실행 전 Mock 객체 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito의 Mock 객체 초기화
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    }

    @Test
    @DisplayName("로그인 폼 - 성공")
    void userLoginForm() throws Exception {
        mockMvc.perform(get("/views/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    @DisplayName("회원가입 폼 - 성공")
    void userSignUpForm() throws Exception {
        mockMvc.perform(get("/views/users/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(model().attributeExists("createUserRequest"));
    }

    @Test
    @DisplayName("마이페이지 폼 - 성공")
    void userMyPage() throws Exception {
        mockMvc.perform(get("/views/users/mypage"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/mypage"));
    }

    @Test
    @DisplayName("로그아웃 - 성공")
    void userLogoutSuccess() throws Exception {
        mockMvc.perform(post("/api/users/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void userSignUpSuccess() {
        // Given: 회원 가입 요청 객체와 Mock 설정
        CreateUserRequest request = CreateUserRequest.builder()
                .username("test@test.com")
                .password("password123")
                .nickname("Tester")
                .build();

        // 사용자 이름이 중복되지 않음을 Mock으로 설정
        given(userService.isDuplicate(request.getUsername())).willReturn(true);
        // 비밀번호를 암호화하여 반환하는 Mock 설정
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodedPassword");

        // When: 회원 가입 요청 처리
        String result = userController.userSignUp(request, bindingResult);

        // Then: 요청이 성공적으로 처리되어 리다이렉트되는지 검증
        assertThat(result).isEqualTo("redirect:/");
        verify(userService).createUser(any(CreateUserRequest.class)); // createUser 메서드가 호출되었는지 확인
    }



}