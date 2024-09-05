package com.est.jobshin.security;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.global.security.config.SecurityConfig;
import com.est.jobshin.global.security.model.CustomUserDetails;
import com.est.jobshin.global.security.provider.UserAuthenticationProvider;
import com.est.jobshin.global.security.service.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SecurityConfig.class) // SecurityConfig 테스트
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc 사용

    @MockBean
    private UserDetailService userDetailService; // UserDetailService 모킹

    @MockBean
    private UserAuthenticationProvider authenticationProvider; // 인증 제공자 모킹

    @Mock
    private UserRepository userRepository; // UserRepository 모킹

    @Mock
    private PasswordEncoder passwordEncoder; // PasswordEncoder 모킹

    @InjectMocks
    private UserAuthenticationProvider userAuthenticationProvider; // 인증 제공자 주입

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 객체 초기화
        userAuthenticationProvider = new UserAuthenticationProvider(userDetailService, passwordEncoder); // 의존성 주입
    }

    @Test
    @DisplayName("사용자 인증 실패 테스트")
    void testUserAuthenticationFailure() {
        // 사용자 인증 실패 시 예외 발생 설정
        given(userDetailService.loadUserByUsername(anyString()))
                .willThrow(new BadCredentialsException("아이디 또는 비밀번호가 잘못 작성 되었습니다."));

        // 인증 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("test@test.com", "wrongPassword");

        // 인증 실패 예외 검증
        assertThatThrownBy(() -> userAuthenticationProvider.authenticate(authenticationToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("아이디 또는 비밀번호가 잘못 작성 되었습니다.");
    }

    @Test
    @DisplayName("사용자 로그인 실패 핸들러 테스트")
    void testAuthenticationFailureHandler() throws Exception {
        // 로그인 실패 시도
        mockMvc.perform(post("/views/users/login")
                        .param("username", "test@test.com")
                        .param("password", "wrongPassword")
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().is3xxRedirection()) // 리다이렉션 기대
                .andExpect(result -> assertThat(result.getResponse().getRedirectedUrl())
                        .contains("/views/users/login?error=")); // 리다이렉션 URL 검증
    }

    @Test
    @DisplayName("비밀번호 인코딩 테스트")
    void testPasswordEncoding() {
        // 비밀번호 인코딩 설정
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword";

        given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);

        // 인코딩 결과 검증
        String result = passwordEncoder.encode(rawPassword);
        assertThat(result).isEqualTo(encodedPassword);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    @DisplayName("사용자 정보 조회 성공 테스트")
    void testLoadUserByUsername() {
        // 사용자 정보 설정
        String username = "test@test.com";
        User user = User.builder()
                .username(username)
                .password("password123")
                .build();

        // 사용자 조회 설정
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        List<GrantedAuthority> authorities = List.of(() -> "ROLE_USER");
        CustomUserDetails expectedUserDetails = new CustomUserDetails(UserResponse.toDto(user), authorities);
        given(userDetailService.loadUserByUsername(username)).willReturn(expectedUserDetails);

        // 조회 결과 검증
        CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(username);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    @WithMockUser(username = "test@test.com", roles = "USER")
    void testLogoutSuccess() throws Exception {
        // 로그아웃 성공 검증
        mockMvc.perform(post("/api/users/logout")
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 코드 확인
                .andExpect(redirectedUrl("/")); // 로그아웃 후 리다이렉트 확인
    }
}

