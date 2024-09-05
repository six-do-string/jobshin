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
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
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
        //-1
    void userLoginForm() throws Exception {
        mockMvc.perform(get("/views/users/login")) // mockMvc를 사용하여  GET 요청 실행
                .andExpect(status().isOk()) // 응답 상태 200 OK 확인
                .andExpect(view().name("user/login")); // 반환된 뷰 이름 확인
    }

    @Test
    @DisplayName("회원가입 폼 - 성공")
        //-2
    void userSignUpForm() throws Exception {
        mockMvc.perform(get("/views/users/signup")) // mockMvc를 사용하여  GET 요청 실행
                .andExpect(status().isOk()) // 응답 상태 200 OK 확인
                .andExpect(view().name("user/signup")) // 반환된 뷰 이름 확인
                .andExpect(model().attributeExists("createUserRequest")); // 모델에 createUserRequest 속성이 있는지 확인
    }

    @Test
    @DisplayName("마이페이지 폼 - 성공")
        //-3
    void userMyPage() throws Exception {
        // 마이페이지 요청을 보낸 후 상태와 뷰 이름을 검증
        mockMvc.perform(get("/views/users/mypage"))
                .andExpect(status().isOk()) // 상태 코드 200(성공) 확인
                .andExpect(view().name("user/mypage")); // 뷰 이름이 "user/mypage"인지 확인
    }

    @Test //-4-1
    @DisplayName("회원 정보 수정 페이지 접근 - 로그인되지 않은 경우 리디렉션")
    void userEditFormNotLoggedIn() throws Exception {
        // Given: 로그인 정보가 없는 상태로 설정
        SecurityContextHolder.clearContext(); // SecurityContext를 비워 로그인 상태를 없앰

        // When & Then: 로그인되지 않은 상태에서 "/views/users/edit" 페이지 접근 시 로그인 페이지로 리디렉션되는지 확인
        mockMvc.perform(get("/views/users/edit"))
                .andExpect(status().is3xxRedirection()) // 리디렉션 상태 코드인지 확인
                .andExpect(redirectedUrl("/views/users/login")); // 리디렉션되는 URL이 로그인 페이지인지 확인
    }

    @Test //-4-2
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
        //-5
    void userLogoutSuccess() throws Exception {
        mockMvc.perform(post("/api/users/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    @DisplayName("회원 탈퇴 페이지 요청- 성공")
        //6-1
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
        //6-2
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
        //-7-1
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
        //7-2
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

    //------------------------
    // 유효성 검사 실패 테스트 --1
    @Test
    @DisplayName("회원정보 수정 완료 요청 - 유효성 검사 실패")
    void userEditValidationError() throws Exception {

        /*UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .nickname("nickname")
                .password("qwer123!")
                .language(Language.JAVASCRIPT)
                .position(Position.FULLSTACK).build();*/

        // Given: 가데이터로 사용할 UserResponse 객체 생성 및 초기화
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .username("testUser@email.com")
                .password("encodedPassword!")
                .nickname("Tester")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

//        when(userService.updateUser("testUser@email.com", updateUserRequest))).thenReturn(userResponse);

        // CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(userResponse, List.of());

        logger.info("userDetails: " + userDetails);
        logger.info("userResponse in userDetails: " + userDetails.getUserResponse());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);

        when(userService.findByUsername("testUser@email.com")).thenReturn(userResponse);

        // When: 잘못된 회원정보 수정 요청 (비밀번호가 너무 짧음)
        mockMvc.perform(put("/api/users/edit")
                        .with(user(userDetails))
                        .param("password", "123") // 잘못된 비밀번호로 유효성 검사 실패 유도
                        .param("nickname", "UpdatedNickname")
                        .param("language", "JAVA")
                        .param("position", "BACKEND")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit"))
                .andExpect(model().attributeExists("bindingResult"))
                .andExpect(model().attributeHasFieldErrors("updateUserRequest", "password"));

        SecurityContextHolder.clearContext();
    }

    // 성공적인 회원정보 수정 요청 테스트-- 1
    /*에러 메세지 읽어보니까 UserResponse에서 Username이 null로 나오는데
     * 이게 지금 회원 */
    @Test
    @DisplayName("회원정보 수정완료 요청 - 성공")
    void userEditSuccess() throws Exception {
        // Given: 가데이터로 사용할 UserResponse 객체 생성 및 초기화
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .username("testUser@email.com")
                .password("encodedPassword!")
                .nickname("Tester")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // GrantedAuthority 리스트 생성
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(userResponse, authorities);

        // 비밀번호 암호화 Mock 설정
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        doNothing().when(userService).updateUser(eq("testUser@email.com"), any(UpdateUserRequest.class));

        // When: 회원정보 수정 요청
        mockMvc.perform(put("/api/users/edit")
                        .param("password", "newPassword123!")
                        .param("nickname", "UpdatedNickname")
                        .param("language", "JAVA")
                        .param("position", "BACKEND")
                        .with(user(userDetails))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Then: 수정 메서드 호출 검증
        verify(userService).updateUser(eq("testUser@email.com"), any(UpdateUserRequest.class));
    }

    @Test
    @DisplayName("회원정보 수정완료 요청 - 성공")
    void userEditSuccess1() throws Exception {
        // Given: 필요한 Mock 설정
        // 비밀번호 암호화 Mock 설정
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        doNothing().when(userService).updateUser(eq("testUser"), any(UpdateUserRequest.class));

        // `CustomUserDetails`를 가데이터 없이 직접 생성
        // Mock 설정으로 필요한 값만 반환하도록 설정
        UserResponse mockUserResponse = mock(UserResponse.class);
        given(mockUserResponse.getUsername()).willReturn("testUser");

        CustomUserDetails userDetails = new CustomUserDetails(mockUserResponse, List.of());

        // When: 회원정보 수정 요청
        mockMvc.perform(put("/api/users/edit")
                        .param("password", "newPassword123") // UpdateUserRequest의 필드명과 일치시켜야 함
                        .param("nickname", "UpdatedNickname")
                        .param("language", "JAVA")
                        .param("position", "BACKEND")
                        .with(user(userDetails)) // 로그인 사용자 정보 설정
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 코드 확인
                .andExpect(redirectedUrl("/"));

        // Then: 수정 메서드 호출 검증
        verify(userService).updateUser(eq("testUser"), any(UpdateUserRequest.class));
    }


    @Test
    @DisplayName("면접 (연습모드) 이력 리스트 - 성공")
    void listInterviewsSuccess() throws Exception {
        // Given: Mock 데이터 설정
        Long mockUserId = 1L;
        UserResponse mockUserResponse = UserResponse.builder()
                .id(mockUserId)
                .username("testUser")
                .build();
        CustomUserDetails userDetails = new CustomUserDetails(mockUserResponse, List.of());

        InterviewHistorySummaryResponse mockResponse = InterviewHistorySummaryResponse.builder()
                .id(1L)
                .title("Mock Interview")
                .nickname("testUser")
                .createdAt(null)
                .score(85L)
                .category("LANGUAGE")
                .build();

        Page<InterviewHistorySummaryResponse> mockPage = new PageImpl<>(
                Collections.singletonList(mockResponse),
                PageRequest.of(0, 10),
                1
        );

        given(userService.getPaginatedInterviews(any(Pageable.class), eq(mockUserId), eq(Mode.PRACTICE)))
                .willReturn(mockPage);

        // When & Then: 요청을 보내고 결과 검증
        mockMvc.perform(get("/views/users/interviews/practice")
                        .param("page", "1")
                        .param("size", "10")
                        .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/practice_interview_list"))
                .andExpect(model().attributeExists("interviewSummaryList"))
                .andExpect(model().attributeExists("pageNumbers"));
    }

    @Test
    @DisplayName("유저 면접(실전모드) 상세 이력 리스트 - 성공")
    void realInterviewsSuccess() throws Exception {
        // Given: 가데이터로 사용할 UserResponse 객체 생성 및 초기화
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .username("testUser")
                .password("encodedPassword")
                .nickname("Tester")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // CustomUserDetails 객체 생성
        CustomUserDetails userDetails = new CustomUserDetails(userResponse, List.of());

        // Mock 데이터 설정
        InterviewHistorySummaryResponse mockResponse = InterviewHistorySummaryResponse.builder()
                .id(1L)
                .title("Mock Interview")
                .nickname("testUser")
                .createdAt(LocalDateTime.now())
                .score(85L)
                .category("LANGUAGE")
                .build();

        Page<InterviewHistorySummaryResponse> mockPage = new PageImpl<>(
                Collections.singletonList(mockResponse),
                PageRequest.of(0, 10),
                1
        );

        // Mock 설정
        given(userService.getPaginatedInterviews(any(Pageable.class), eq(1L), eq(Mode.REAL)))
                .willReturn(mockPage);

        // When & Then: 요청을 보내고 결과 검증
        mockMvc.perform(get("/views/users/interviews/real")
                        .param("page", "1")
                        .param("size", "10")
                        .with(user(userDetails))) // 로그인 사용자 정보 설정
                .andExpect(status().isOk())
                .andExpect(view().name("user/real_interview_list"))
                .andExpect(model().attributeExists("interviewSummaryList"))
                .andExpect(model().attributeExists("pageNumbers"));
    }

    @Test
    @DisplayName("실전 모드 상세 정보 조회 - 성공")
    void realInterviewDetailSuccess() throws Exception {
        // Given: Mock 데이터 설정
        MyPageInterviewWithDetailsDto mockDetails = MyPageInterviewWithDetailsDto.builder()
                .id(1L)
                .averageScore(90)
                .build();

        given(userService.getInterviewDetail(anyLong())).willReturn(mockDetails);

        // When & Then: 요청을 보내고 결과 검증
        mockMvc.perform(get("/views/users/interviews/real/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/real_interview_detail"))
                .andExpect(model().attributeExists("interviewDetails"));
    }

    @Test
    @DisplayName("연습 모드 상세 정보 조회 - 성공")
    void practiceInterviewDetailSuccess() throws Exception {
        // Given: Mock 데이터 설정
        MyPageInterviewWithDetailsDto mockDetails = MyPageInterviewWithDetailsDto.builder()
                .id(1L)
                .averageScore(85)
                .build();

        given(userService.getInterviewDetail(anyLong())).willReturn(mockDetails);

        // When & Then: 요청을 보내고 결과 검증
        mockMvc.perform(get("/views/users/interviews/practice/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/practice_interview_detail"))
                .andExpect(model().attributeExists("interviewDetails"));
    }
}


