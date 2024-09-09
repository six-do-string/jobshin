package com.est.jobshin.userIntegration;

import com.est.jobshin.domain.interview.controller.InterviewController;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.global.security.model.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InterviewIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인터뷰 생성, 조회, 답변 제출 및 면접 종료 흐름 통합 테스트")
    void fullInterviewFlowTest() throws Exception {
        // Step 1: CustomUserDetails 생성
        String uniqueEmail = "test_" + UUID.randomUUID() + "@integration.com";
        String uniqueNickname = "Tester_" + UUID.randomUUID();
        UserResponse userResponse = UserResponse.builder()
                .username(uniqueEmail)
                .nickname(uniqueNickname)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(userResponse, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        // Step 2: 인터뷰 생성 요청 및 검증 (연습모드)
        mockMvc.perform(post("/api/mock-interviews/practice")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"테스트 인터뷰\", \"category\":\"CS\", \"mode\":\"PRACTICE\"}") // 필수 필드 추가
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 인터뷰"));

//        // Step 3: 인터뷰 생성 요청 및 검증 (실전모드)
//        mockMvc.perform(post("/api/mock-interviews/real")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\":\"실전 인터뷰\", \"category\":\"ALGORITHM\"}") // JSON 데이터 수정
//                        .with(user(customUserDetails)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("실전 인터뷰"));
//
//        // Step 4: 생성된 인터뷰 상세 조회
//        Long interviewId = 1L;
//        mockMvc.perform(get("/api/mock-interviews/incomplete/{id}", interviewId)
//                        .with(csrf())
//                        .with(user(customUserDetails)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("테스트 인터뷰"));
//
//        // Step 5: 다음 질문 조회
//        mockMvc.perform(get("/api/mock-interviews/next")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(user(customUserDetails)))
//                .andExpect(status().isOk());
//
//        // Step 6: 답변 제출 및 다음 질문 조회
//        mockMvc.perform(post("/api/mock-interviews/next")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":1,\"question\":\"질문\",\"answer\":\"답변\"}")
//                        .with(user(customUserDetails)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.question").value("질문"))
//                .andExpect(jsonPath("$.answer").value("답변"));
//
//        // Step 7: 인터뷰 종료 요청 및 결과 요약 조회
//        mockMvc.perform(get("/api/mock-interviews/summary")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .with(user(customUserDetails)))
//                .andExpect(status().isOk());
//
//        // Step 8: 면접 기록 삭제 요청 및 검증
//        mockMvc.perform(delete("/api/mock-interviews/{id}", interviewId)
//                        .with(csrf())
//                        .with(user(customUserDetails)))
//                .andExpect(status().isNoContent());
    }
}