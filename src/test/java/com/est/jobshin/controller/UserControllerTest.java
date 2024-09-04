package com.est.jobshin.controller;

import com.est.jobshin.domain.user.controller.UserController;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;



import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build(); // MockMvc 설정: UserController를 테스트하기 위한 standalone 환경 구성

    }

    @Test
    @DisplayName("로그인 폼 - 성공")
    void userLoginForm() throws Exception {
        mockMvc.perform(get("/views/users/login")) // mockMvc를 사용하여  GET 요청 실행
                .andExpect(status().isOk()) // 응답 상태 200 OK 확인
                .andExpect(view().name("user/login")); // 반환된 뷰 이름 확인
    }

    @Test
    @DisplayName("회원가입 폼 - 성공")
    void userSignUpForm() throws Exception {
        mockMvc.perform(get("/views/users/signup")) // mockMvc를 사용하여  GET 요청 실행
                .andExpect(status().isOk()) // 응답 상태 200 OK 확인
                .andExpect(view().name("user/signup")) // 반환된 뷰 이름 확인
                .andExpect(model().attributeExists("createUserRequest")); // 모델에 createUserRequest 속성이 있는지 확인
    }

    @Test
    @DisplayName("마이페이지 폼 - 성공")
    void userMyPage() throws Exception {
        // 마이페이지 요청을 보낸 후 상태와 뷰 이름을 검증
        mockMvc.perform(get("/views/users/mypage"))
                .andExpect(status().isOk()) // 상태 코드 200(성공) 확인
                .andExpect(view().name("user/mypage")); // 뷰 이름이 "user/mypage"인지 확인
    }

    @Test
    @DisplayName("회원 정보 수정 페이지 접근 - 로그인되지 않은 경우 리디렉션")
    void userEditFormNotLoggedIn() throws Exception {
        // Given: 로그인 정보가 없는 상태로 설정
        SecurityContextHolder.clearContext(); // SecurityContext를 비워 로그인 상태를 없앰

        // When & Then: 로그인되지 않은 상태에서 "/views/users/edit" 페이지 접근 시 로그인 페이지로 리디렉션되는지 확인
        mockMvc.perform(get("/views/users/edit"))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드인지 확인
                .andExpect(redirectedUrl("/views/users/login")); // 리디렉션되는 URL이 로그인 페이지인지 확인
    }

    @Test
    @DisplayName("회원 정보 수정 페이지 접근 - 성공")
    void userEditFormSuccess() throws Exception {
        // Given: 로그인된 사용자 정보 설정
        CustomUserDetails userDetails = new CustomUserDetails(
                UserResponse.builder()
                        .username("testUser")
                        .nickname("Tester")
                        .build(),
                Collections.emptyList() // 빈 권한 목록
        );

        // Mock 설정: 로그인된 사용자 정보가 SecurityContext에 있는 것처럼 설정
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
        SecurityContextHolder.setContext(securityContext);

        // Mock 설정: 사용자의 정보가 UserResponse로 반환되도록 설정
        given(userService.findByUsername("testUser")).willReturn(
                UserResponse.builder().username("testUser").nickname("Tester").build()
        );

        // When & Then: "/views/users/edit" 페이지 접근 시 성공적으로 이동되는지 확인
        mockMvc.perform(get("/views/users/edit"))
                .andExpect(status().isOk()) // 상태 코드가 200 OK인지 확인
                .andExpect(view().name("user/edit")) // 반환되는 뷰가 "user/edit"인지 확인
                .andExpect(model().attributeExists("updateUserRequest")); // 모델에 updateUserRequest가 있는지 확인
    }


    @Test
    @DisplayName("로그아웃 - 성공")
    void userLogoutSuccess() throws Exception {
        mockMvc.perform(post("/api/users/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    @DisplayName("회원 탈퇴 페이지 요청- 성공")
    void userDeleteSuccess() throws Exception {
        // Given: Mock 설정 - SecurityContext에서 인증 정보 가져오기
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.isAuthenticated()).willReturn(true);

        // Given: UserService의 deleteUser 메서드를 호출할 때 아무 일도 하지 않도록 설정
        doNothing().when(userService).deleteUser(anyString());

        // When: 회원 탈퇴 요청을 보냄
        mockMvc.perform(delete("/api/users/{username}", "testUser")
                        .requestAttr("request", request)
                        .requestAttr("response", response))
                .andExpect(status().isNoContent());

        // Then: 로그아웃 처리 및 deleteUser 메서드 호출 확인
        verify(userService).deleteUser("testUser");
        verify(authentication).isAuthenticated();
    }

    @Test
    @DisplayName("회원 탈퇴 페이지 요청- 인증되지 않은 사용자")
    void userDeleteUnauthenticated() throws Exception {
        // Given: 인증되지 않은 사용자
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        given(securityContext.getAuthentication()).willReturn(authentication);
        given(authentication.isAuthenticated()).willReturn(false);

        // When: 인증되지 않은 사용자로 회원 탈퇴 요청을 보냄
        mockMvc.perform(delete("/api/users/{username}", "testUser")
                        .requestAttr("request", request)
                        .requestAttr("response", response))
                .andExpect(status().isNoContent());

        // Then: deleteUser는 호출되지만, 로그아웃 처리는 되지 않음
        verify(userService).deleteUser("testUser");
        verify(authentication).isAuthenticated();
    }


    @Test
    @DisplayName("회원 가입 성공 테스트")
    void userSignUpSuccesstest() {
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


    @Test
    @DisplayName("회원가입 요청 - 유효성 검사 실패")
    void userSignUpValidationFailure() throws Exception {
        // When & Then: 잘못된 데이터로 회원가입 요청 수행 (비밀번호가 너무 짧음)
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "invalid@example.com")
                        .param("password", "123") // 비밀번호 유효성 검사 실패
                        .param("nickname", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/signup"))
                .andExpect(model().attributeHasFieldErrors("createUserRequest", "password"));
    }

    @Test
    @DisplayName("회원가입 요청 - 성공")
    void userSignUpSuccess() throws Exception {
        // Given
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test@example.com");
        createUserRequest.setPassword("password123");
        createUserRequest.setNickname("TestUser");

        given(userService.isDuplicate(anyString())).willReturn(true);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        doNothing().when(userService).createUser(any(CreateUserRequest.class));

        // When & Then
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", createUserRequest.getUsername())
                        .param("password", createUserRequest.getPassword())
                        .param("nickname", createUserRequest.getNickname()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}