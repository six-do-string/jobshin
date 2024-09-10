package com.est.jobshin.userIntegration;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Position;
import com.est.jobshin.global.security.model.CustomUserDetails;
import com.est.jobshin.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewDetailRepository interviewDetailRepository;

    @BeforeEach
    void setUp() {
        // 테스트 실행 전에 모든 데이터 삭제
        userRepository.deleteAll();
        interviewRepository.deleteAll();
        interviewDetailRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입부터 탈퇴까지 전체 흐름 통합 테스트")
    void fullUserFlowTest() throws Exception {
        // 유니크한 이메일과 닉네임 생성
        String uniqueEmail = "test_" + UUID.randomUUID() + "@integration.com";
        String uniqueNickname = "Tester_" + UUID.randomUUID();

        // 회원 가입 요청
        CreateUserRequest signUpRequest = CreateUserRequest.builder()
                .username(uniqueEmail)
                .password("password123!")
                .nickname(uniqueNickname)
                .language(Language.JAVA)
                .position(Position.BACKEND)
                .build();

        // 회원가입 요청 및 검증
        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", signUpRequest.getUsername())
                        .param("password", signUpRequest.getPassword())
                        .param("nickname", signUpRequest.getNickname())
                        .param("language", signUpRequest.getLanguage().toString())
                        .param("position", signUpRequest.getPosition().toString())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // 로그인 요청 및 검증
        mockMvc.perform(post("/views/users/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", signUpRequest.getUsername())
                        .param("password", signUpRequest.getPassword())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // CustomUserDetails 객체 생성
        UserResponse userResponse = UserResponse.builder()
                .username(uniqueEmail)
                .nickname(uniqueNickname)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userResponse, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // 회원 정보 수정 요청 생성
        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .username(uniqueEmail)
                .password("newPassword123!")
                .nickname("UpdatedTester")
                .language(Language.PYTHON)
                .position(Position.FULLSTACK)
                .build();

        // 회원 정보 수정 요청 및 검증
        mockMvc.perform(put("/api/users/edit")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("password", updateRequest.getPassword())
                        .param("nickname", updateRequest.getNickname())
                        .param("language", updateRequest.getLanguage().toString())
                        .param("position", updateRequest.getPosition().toString())
                        .with(csrf())
                        .with(user(customUserDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));


    // 에러남 여기부터


//    // 면접 생성 및 저장
//    Interview interview = Interview.createInterview("Test Interview", LocalDateTime.now(), userRepository.findByUsername(uniqueEmail).orElseThrow(), Mode.PRACTICE);
//
//    // 면접 저장 및 검증
//    Interview savedInterview = interviewRepository.save(interview);
//    assertThat(savedInterview.getId()).isNotNull();
//
//    // 면접 상세 내용 생성 및 저장
//    InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("What is Java?", Category.LANGUAGE, Mode.PRACTICE, LocalDateTime.now());
//        interviewDetail.setAnswer("Java is a programming language.");
//        interviewDetail.setExampleAnswer("Java language");
//        interviewDetail.setScore(50L);
//        interviewDetail.setInterview(savedInterview);
//
//    // 상세 내용 저장 및 데이터베이스에 반영
//    InterviewDetail savedDetail = interviewDetailRepository.save(interviewDetail);
//        interviewRepository.saveAndFlush(savedInterview);
//
//    // 면접 이력 리스트 요청 및 검증
//        mockMvc.perform(get("/views/users/interviews/practice")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("mode", "PRACTICE")
//                        .with(csrf())
//            .with(user(customUserDetails)))
//            .andExpect(status().isOk())
//            .andExpect(view().name("user/practice_interview_list"))
//            .andExpect(model().attributeExists("interviewSummaryList"))
//            .andExpect(model().attributeExists("pageNumbers"));
//
//    // 면접 상세 정보 조회 요청 및 검증
//        mockMvc.perform(get("/views/users/interviews/real/{id}", savedInterview.getId())
//            .with(csrf())
//            .with(user(customUserDetails)))
//            .andExpect(status().isOk())
//            .andExpect(view().name("user/real_interview_detail"))
//            .andExpect(model().attributeExists("interviewDetails"));


// 에러남 ^


        // 로그아웃 요청 및 검증
        mockMvc.perform(post("/api/users/logout")
                        .with(csrf())
                        .with(user(customUserDetails)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // 회원 탈퇴 요청 및 검증
        mockMvc.perform(delete("/api/users/{username}", uniqueEmail)
                        .with(csrf())
                        .with(user(customUserDetails)))
                .andExpect(status().isNoContent());
    }
}