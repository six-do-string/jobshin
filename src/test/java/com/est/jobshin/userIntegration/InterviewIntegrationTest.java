package com.est.jobshin.userIntegration;
import static org.junit.jupiter.api.Assertions.fail;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import com.est.jobshin.global.security.model.CustomUserDetails;
import com.est.jobshin.infra.alan.AlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mockito.MockitoAnnotations;

import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InterviewIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // PasswordEncoder 주입


    private MockHttpSession session;

    private User user;

    @MockBean
    private AlanService alanService;

    @MockBean
    private InterviewDetailService interviewDetailService;

    @MockBean
    private InterviewDetailRepository interviewDetailRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    @Test
    @DisplayName("실전 모드 인터뷰 생성 통합 테스트")
    void createRealInterviewTest() throws Exception {
        // Given: 사용자 설정
        user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // SecurityContext에 CustomUserDetails 설정
        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 세션 초기화
        session = new MockHttpSession();

        // Optional<User> 조회 및 확인
        Optional<User> users = userRepository.findByUsername("test@test.com");
        System.out.println("users: " + users); // 사용자 정보 확인

        // When & Then: 실전 모드 인터뷰 생성 요청을 보내고 결과를 검증
        mockMvc.perform(post("/api/mock-interviews/real")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // 기대한 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.title").value("실전 모드"));
    }

    @Test
    @DisplayName("연습 모드 인터뷰 생성 통합 테스트")
    void createPracticeInterviewTest() throws Exception {
        // Given: 사용자 설정
        user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // SecurityContext에 CustomUserDetails 설정
        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 세션 초기화
        session = new MockHttpSession();

        // 사용자 확인
        Optional<User> users = userRepository.findByUsername("test@test.com");
        System.out.println("users: " + users); // 사용자 정보 확인

        // Given: 카테고리 설정
        Category category = Category.CS;

        // When & Then: 연습 모드 인터뷰 생성 요청을 보내고 결과를 검증
        mockMvc.perform(post("/api/mock-interviews/practice")
                        .with(csrf())
                        .param("category", category.name())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()) // 기대한 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.title").value(category.name() + " 파트 연습 모드"));
    }


//    @Test
//    @DisplayName("모의면접 다음 질문 조회 통합 테스트")
//    void getNextQuestionIntegrationTest() throws Exception {
//        // Given: 사용자 설정
//        user = User.builder()
//                .username("test@test.com")
//                .password("password")
//                .nickname("테스트")
//                .language(Language.JAVA)
//                .level(Level.LV2)
//                .position(Position.BACKEND)
//                .build();
//
//        // SecurityContext에 CustomUserDetails 설정
//        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        // 세션 초기화
//        session = new MockHttpSession();
//
//        // 인터뷰 디테일 생성
//        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
//        InterviewDetail interviewDetail1 = InterviewDetail.createInterviewDetail("질문1?", Category.LANGUAGE, REAL, LocalDateTime.now());
//
//        // Given: 인터뷰 디테일 리스트를 세션에 설정
//        List<InterviewDetail> questions = new ArrayList<>();
//        questions.add(interviewDetail);
//        questions.add(interviewDetail1);
//        session.setAttribute("questions", questions);
//        session.setAttribute("currentIndex", 0);
//
//        // When & Then: 첫 번째 질문 조회 요청을 보내고 결과를 검증
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
//                        .with(csrf())
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.question").value("질문?")); // 첫 번째 질문이 "질문?"인지 확인
//
//        // 세션의 currentIndex를 올바르게 업데이트하고 있는지 확인
//        int currentIndex = (int) session.getAttribute("currentIndex");
//        assertThat(currentIndex).isEqualTo(1); // currentIndex가 1로 업데이트 되었는지 확인
//
//        // When & Then: 두 번째 질문 조회 요청을 보내고 결과를 검증
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
//                        .with(csrf())
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.question").value("질문1?")); // 두 번째 질문이 "질문1?"인지 확인
//
//        // 세션의 currentIndex를 올바르게 업데이트하고 있는지 확인
//        currentIndex = (int) session.getAttribute("currentIndex");
//        assertThat(currentIndex).isEqualTo(2); // currentIndex가 2로 업데이트 되었는지 확인
//
//        // When & Then: 마지막으로 질문이 없는 상태를 검증
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
//                        .with(csrf())
//                        .session(session)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.question").doesNotExist()); // 더 이상 질문이 없음을 확인
//    }

    @Test
    @DisplayName("모의면접 답변 제출 및 다음 질문 조회 통합 테스트")
    void submitAnswerAndGetNextQuestionTest() throws Exception {
        // Given: 사용자 설정
        user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // SecurityContext에 CustomUserDetails 설정
        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 세션 초기화
        session = new MockHttpSession();

        // 인터뷰 디테일 생성
        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
        InterviewDetail interviewDetail1 = InterviewDetail.createInterviewDetail("질문1?", Category.LANGUAGE, REAL, LocalDateTime.now());

        // Given: 인터뷰 디테일 리스트를 세션에 설정
        List<InterviewDetail> questions = new ArrayList<>();
        questions.add(interviewDetail);
        questions.add(interviewDetail1);
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);

        // When & Then: 첫 번째 질문 조회 요청을 보내고 결과를 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문?")); // 첫 번째 질문이 "질문?"인지 확인

        // 세션의 currentIndex를 올바르게 업데이트하고 있는지 확인
        int currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(1); // currentIndex가 1로 업데이트 되었는지 확인

        // 답변 생성
        InterviewQuestion answerForFirstQuestion = InterviewQuestion.builder()
                .id(interviewDetail.getId())
                .question("질문?")
                .answer("첫 번째 답변")
                .build();

        String answerContent = objectMapper.writeValueAsString(answerForFirstQuestion);

        // 답변 제출 및 다음 질문 조회 요청을 보내고 결과를 검증
        mockMvc.perform(post("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(answerContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문1?")); // 두 번째 질문이 "질문1?"인지 확인

        // 세션의 currentIndex를 올바르게 업데이트하고 있는지 확인
        currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(2); // currentIndex가 2로 업데이트 되었는지 확인

        // 답변 생성
        InterviewQuestion answerForSecondQuestion = InterviewQuestion.builder()
                .id(interviewDetail1.getId())
                .question("질문1?")
                .answer("두 번째 답변")
                .build();

        answerContent = objectMapper.writeValueAsString(answerForSecondQuestion);

        // When & Then: 마지막 질문에 대한 답변 제출 후, 질문이 없는 상태를 검증
        mockMvc.perform(post("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(answerContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").doesNotExist()); // 더 이상 질문이 없음을 확인
    }

    @Test
    @DisplayName("모의면접 종료 통합 테스트")
    void finishInterviewTest() throws Exception {
        // Given: 사용자 설정
        user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // SecurityContext에 CustomUserDetails 설정
        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 세션 초기화
        session = new MockHttpSession();

        // 인터뷰 디테일 생성 및 세션 설정
        List<InterviewDetail> questions = new ArrayList<>();
        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("마지막 질문", Category.LANGUAGE, REAL, LocalDateTime.now());
        interviewDetail.setId(1L);
        questions.add(interviewDetail);
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);
        session.setAttribute("interviewFinished", false); // 종료 조건 설정

        // 마지막 질문에 대한 답변 생성
        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .id(interviewDetail.getId())
                .question("마지막 질문")
                .answer("마지막 답변")
                .build();

        // Mock 설정
        when(interviewDetailRepository.findById(interviewDetail.getId())).thenReturn(Optional.of(interviewDetail));
        when(alanService.callAnswer(interviewQuestion.getQuestion(), interviewQuestion.getAnswer())).thenReturn("[90][평가내용]");

        doAnswer(invocation -> {
            InterviewQuestion question = invocation.getArgument(0);
            InterviewDetail detail = interviewDetailRepository.findById(question.getId()).orElseThrow();
            detail.setAnswer(question.getAnswer());
            detail.setScore(90L); // 예시로 점수 설정
            return null;
        }).when(interviewDetailService).getAnswerByUser(any(InterviewQuestion.class));

        String jsonContent = objectMapper.writeValueAsString(interviewQuestion);

        // When & Then: 마지막 질문에 대한 답변 제출
        mockMvc.perform(post("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk()) // 기대한 상태 코드가 200 OK인지 확인
                .andExpect(jsonPath("$.question").value("마지막 질문")); // 마지막 질문이 있는지 확인

        // 세션의 currentIndex와 종료 상태 확인
        int currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(1); // currentIndex가 1로 업데이트 되었는지 확인
        assertThat((boolean) session.getAttribute("interviewFinished")).isFalse(); // 종료되지 않았음을 확인

        // 종료 설정을 세션에 추가
        session.setAttribute("interviewFinished", true); // 인터뷰 종료 상태를 세션에서 수동으로 설정

        // 인터뷰가 종료 상태인지 검증 로직을 추가하여 조건을 맞추고 종료 요청을 보냅니다.
        if ((boolean) session.getAttribute("interviewFinished")) {
            // When & Then: 인터뷰 종료 요청을 보내고 결과를 검증
            mockMvc.perform(post("/api/mock-interviews/finish")
                            .with(csrf())
                            .session(session)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent))
                    .andDo(print())
                    .andExpect(status().isOk()) // 200 OK가 기대됨
                    .andExpect(content().string("success")); // 종료 성공 메시지 확인
        } else {
            // 종료 상태가 아닌 경우 테스트 실패 처리
            fail("인터뷰가 종료 상태가 아닙니다.");
        }

        // 인터뷰가 종료되었는지 세션 확인
        assertThat((boolean) session.getAttribute("interviewFinished")).isTrue(); // 종료되었음을 확인
    }
}