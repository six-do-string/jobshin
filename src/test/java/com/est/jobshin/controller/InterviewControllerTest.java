package com.est.jobshin.controller;

import com.est.jobshin.domain.interview.controller.InterviewController;
import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.dto.InterviewResultDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = InterviewController.class)
@WithMockUser(username = "test@test.com")
public class InterviewControllerTest {


	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	private Interview interview;
	private Category category;
	private InterviewDto interviewDto;
	private InterviewQuestion interviewQuestion;
	private String string;
	private List<InterviewResultDetail> interviewResultDetails;

	@MockBean
	private InterviewService interviewService;


	@DisplayName("연습모드 모의면접 생성")
	@Test
	public void testCreatePracticeInterview() throws Exception {

		category = Category.CS;
		interview = new Interview();
		interviewDto = InterviewDto.fromInterview(interview);

		when(interviewService.createPracticeInterview(eq(category), any(HttpSession.class)))
			.thenReturn(interview);


		mockMvc.perform(post("/api/mock-interviews/practice")
				.with(csrf())
				.param("category", "CS")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());

	}

	@DisplayName("실전모드 모의면접 생성")
	@Test
	public void testCreateRealInterview() throws Exception {

		interview = new Interview();
		interviewDto = InterviewDto.fromInterview(interview);

		when(interviewService.createRealInterview(any(HttpSession.class)))
			.thenReturn(interview);


		mockMvc.perform(post("/api/mock-interviews/real")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());

	}

	@DisplayName("모의면접 이어하기")
	@Test
	void testContinueIncompleteInterview() throws Exception {

		interview = Interview.createInterview("테스트", null, null, null);
		Long interviewId = 1L;

		interviewDto = InterviewDto.fromInterview(interview);

		when(interviewService.loadIncompleteInterview(eq(interviewId), any(HttpSession.class))).thenReturn(interview);

		mockMvc.perform(get("/api/mock-interviews/incomplete/1")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value(interview.getTitle()));
	}

	@DisplayName("모의면접 다음 질문 조회")
	@Test
	void testGetNextQuestion() throws Exception {

		interviewQuestion = new InterviewQuestion();

		when(interviewService.getNextQuestion(any(HttpSession.class))).thenReturn(interviewQuestion);

		mockMvc.perform(get("/api/mock-interviews/next")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@DisplayName("모의면접 답변 제출 및 다음 질문 조회")
	@Test
	void testSubmitAnswerAndGetNextQuestion() throws Exception {

		interviewQuestion = InterviewQuestion.builder()
			.id(1L)
			.question("질문")
			.answer("답변")
			.build();

		when(interviewService.processAnswerAndGetNextQuestion(any(HttpSession.class), any(InterviewQuestion.class))).thenReturn(interviewQuestion);

		mockMvc.perform(post("/api/mock-interviews/next")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(interviewQuestion))
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.question").value(interviewQuestion.getQuestion()))
			.andExpect(jsonPath("$.answer").value(interviewQuestion.getAnswer()));
	}

	@DisplayName("모의면접 종료")
	@Test
	void testFinishInterview() throws Exception {

		interviewQuestion = InterviewQuestion.builder()
			.id(1L)
			.question("질문")
			.answer("답변")
			.build();

		when(interviewService.lastQuestion(any(InterviewQuestion.class), any(HttpSession.class))).thenReturn(string);

		mockMvc.perform(post("/api/mock-interviews/finish")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(interviewQuestion))
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@DisplayName("모의면접 요약")
	@Test
	void testSummaryInterview() throws Exception {

		interviewResultDetails = new ArrayList<>();

		when(interviewService.summaryInterview(any(HttpSession.class))).thenReturn(interviewResultDetails);

		mockMvc.perform(get("/api/mock-interviews/summary")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());

	}

	@DisplayName("모의면접 기록 삭제")
	@Test
	void testDeleteInterview() throws Exception {

		interview = Interview.createInterview("테스트", null, null, null);
		Long interviewId = 1L;

		interviewService.deleteInterviewsById(interviewId);

		mockMvc.perform(delete("/api/mock-interviews/1")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
		)
			.andDo(print())
			.andExpect(status().isNoContent());

	}

}
