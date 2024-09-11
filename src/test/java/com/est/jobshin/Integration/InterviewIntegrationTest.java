package com.est.jobshin.Integration;

import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import com.est.jobshin.global.security.model.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InterviewIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(InterviewIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockHttpSession session;
    private User user;

    @Autowired
    private InterviewDetailRepository interviewDetailRepository;

    @MockBean
    private InterviewDetailService interviewDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("실전 모드 인터뷰 생성 통합 테스트")
    void createRealInterviewTest() throws Exception {
        // 사용자 설정 및 저장
        user = User.builder()
                .username("test@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        userRepository.save(user);

        // SecurityContext에 사용자 설정
        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 실전 모드 인터뷰 생성 요청 및 검증
        mockMvc.perform(post("/api/mock-interviews/real")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("실전 모드"));
    }

    @Test
    @DisplayName("연습 모드 인터뷰 생성 통합 테스트")
    void createPracticeInterviewTest() throws Exception {
        // 사용자 설정 및 저장
        user = User.builder()
                .username("test@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        userRepository.save(user);

        // SecurityContext에 사용자 설정
        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 연습 모드 인터뷰 생성 요청 및 검증
        mockMvc.perform(post("/api/mock-interviews/practice")
                        .with(csrf())
                        .param("category", Category.CS.name())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(Category.CS.name() + " 파트 연습 모드"));
    }

    @Test
    @DisplayName("모의면접 다음 질문 조회 통합 테스트")
    void getNextQuestionIntegrationTest() throws Exception {
        // 사용자 설정
        user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 인터뷰 디테일 생성
        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
        InterviewDetail interviewDetail1 = InterviewDetail.createInterviewDetail("질문1?", Category.LANGUAGE, REAL, LocalDateTime.now());

        // 세션 설정
        List<InterviewDetail> questions = new ArrayList<>();
        questions.add(interviewDetail);
        questions.add(interviewDetail1);
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);

        // 첫 번째 질문 조회 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문?"));

        int currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(1);

        // 두 번째 질문 조회 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문1?"));

        currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(2);

        // 마지막 질문 없는 상태 확인
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").doesNotExist());
    }

    @Test
    @DisplayName("모의면접 다음 질문 조회 및 답변 제출 통합 테스트")
    void getNextQuestionAndSubmitAnswerIntegrationTest() throws Exception {
        // 사용자 설정 및 저장
        user = User.builder()
                .username("test@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 인터뷰 디테일 생성 및 저장
        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        InterviewDetail interviewDetail1 = InterviewDetail.createInterviewDetail("질문1?", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());

        interviewDetail = interviewDetailRepository.save(interviewDetail);
        interviewDetail1 = interviewDetailRepository.save(interviewDetail1);

        // 세션 설정
        List<InterviewDetail> questions = new ArrayList<>();
        questions.add(interviewDetail);
        questions.add(interviewDetail1);
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);

        // 첫 번째 질문 조회 요청 및 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문?"));

        int currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(1);

        // 답변 제출 및 다음 질문 조회 요청
        InterviewQuestion answerForFirstQuestion = InterviewQuestion.builder()
                .id(interviewDetail.getId())
                .question("질문?")
                .answer("첫 번째 답변")
                .build();

        String answerContent = objectMapper.writeValueAsString(answerForFirstQuestion);

        mockMvc.perform(post("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(answerContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문1?"));

        currentIndex = (int) session.getAttribute("currentIndex");
        assertThat(currentIndex).isEqualTo(2);

        // 두 번째 질문에 대한 답변 제출 및 마지막 질문 확인
        InterviewQuestion answerForSecondQuestion = InterviewQuestion.builder()
                .id(interviewDetail1.getId())
                .question("질문1?")
                .answer("두 번째 답변")
                .build();

        answerContent = objectMapper.writeValueAsString(answerForSecondQuestion);

        mockMvc.perform(post("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(answerContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").doesNotExist());
    }

    @Test
    @DisplayName("모의면접 종료 통합 테스트")
    void finishInterviewTest() throws Exception {
        // 사용자 설정 및 저장
        user = User.builder()
                .username("test@test.com")
                .password(passwordEncoder.encode("password"))
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        userRepository.saveAndFlush(user);

        CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // 인터뷰 디테일 생성 및 저장
        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("마지막 질문", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        InterviewDetail savedInterviewDetail = interviewDetailRepository.saveAndFlush(interviewDetail);

        // 세션 설정
        List<InterviewDetail> questions = new ArrayList<>();
        questions.add(savedInterviewDetail);
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);
        session.setAttribute("interviewId", 1L);

        // 마지막 질문에 대한 답변 생성
        InterviewQuestion interviewQuestion = InterviewQuestion.builder()
                .id(savedInterviewDetail.getId())
                .question("마지막 질문")
                .answer("마지막 답변")
                .build();

        String jsonContent = objectMapper.writeValueAsString(interviewQuestion);

        // 답변 제출 요청
        doAnswer(invocation -> null).when(interviewDetailService).getAnswerByUser(any(InterviewQuestion.class));

        mockMvc.perform(post("/api/mock-interviews/next")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk());

        session.setAttribute("interviewFinished", true);

        // 인터뷰 종료 요청 및 검증
        mockMvc.perform(post("/api/mock-interviews/finish")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}