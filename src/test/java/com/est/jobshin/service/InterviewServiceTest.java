package com.est.jobshin.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.dto.InterviewResultDetail;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

	protected MockHttpSession session;

	@Mock
	private InterviewRepository interviewRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private InterviewDetailRepository interviewDetailRepository;
	@Mock
	private InterviewDetailService interviewDetailService;

	@InjectMocks
	private InterviewService interviewService;

	private User user;
	private Interview interview;
	private InterviewDetail interviewDetail;
	private List<InterviewDetail> interviewDetailList = new ArrayList<>();
	private InterviewQuestion interviewQuestion;
	private InterviewQuestion nextQuestion;
	private InterviewResultDetail interviewResultDetail;
	private List<InterviewResultDetail> interviewResultDetailList = new ArrayList<>();

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
		session = null;
	}

	@DisplayName("면접 조회")
	@Test
	void checkGetInterview() {
		//Given
		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), null, REAL);

		InterviewDto interviewDto = InterviewDto.fromInterview(interview);
		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

		//When
		InterviewDto checkInterview = interviewService.getInterviewById(interview.getId());

		//Then
		assertThatCode(() -> interviewService.getInterviewById(interview.getId()))
			.doesNotThrowAnyException();
	}

	@DisplayName("면접 기록 삭제")
	@Test
	void checkDeleteInterview() {

		//Given
		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), null, REAL);

		//When
		interviewService.deleteInterviewsById(interview.getId());

		//Then
		verify(interviewRepository, times(1)).deleteById(interview.getId());
	}

	@DisplayName("모의면접 연습모드 생성")
	@Test
	void testCreatePracInterview() {

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

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, PRACTICE);
		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, PRACTICE, LocalDateTime.now());

		interviewDetailList.add(interviewDetail);

		session = new MockHttpSession();

		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

		interviewService.createPracticeInterview(interviewDetail.getCategory(), session);

		verify(interviewDetailService, times(1)).practiceModeStarter(any(), any(), any());

	}

	@DisplayName("모의면접 실전모드 생성")
	@Test
	void testCreateRealInterview() {

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


		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		interviewDetailList.add(interviewDetail);

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, REAL);
		interview.addInterviewDetails(interviewDetail);

		session = new MockHttpSession();

		when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
		when(interviewRepository.save(any(Interview.class))).thenReturn(interview);

		interviewService.createRealInterview(session);

		verify(interviewDetailService, times(1)).realModeStarter(any(), any());

	}

	@DisplayName("모의면접 이어하기")
	@Test
	void testLoadIncompleteInterview() {

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		interviewDetailList.add(interviewDetail);

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, REAL);
		interview.addInterviewDetails(interviewDetail);

		session = new MockHttpSession();

		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

		interviewService.loadIncompleteInterview(interview.getId(), session);

		assertThat(interviewDetailList).hasSize(1);
		assertThat(interviewDetailList.get(0)).isEqualTo(interviewDetail);
	}

	@DisplayName("모의면접 진행하기")
	@Test
	void testProcessAnswerAndGetNextQuestion() {
		List<InterviewDetail> questions = new ArrayList<>();

		InterviewDetail interviewDetailFirst = InterviewDetail.createInterviewDetail("질문1", Category.LANGUAGE, REAL, LocalDateTime.now());
		InterviewDetail interviewDetailSecond = InterviewDetail.createInterviewDetail("질문2", Category.LANGUAGE, REAL, LocalDateTime.now());

		questions.add(interviewDetailFirst);
		questions.add(interviewDetailSecond);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("questions", questions);

		//서비스 코드에서 실제로는 getNextQuestion메서드가 1회 생성된 후에
		//processAnswerAndGetNextQuestion메서드가 실행되기 때문에 초기 index를 1로 설정
		session.setAttribute("currentIndex", 1);

		InterviewQuestion interviewQuestion = new InterviewQuestion(interviewDetailFirst.getId(), interviewDetailFirst.getQuestion(), "답변1", 5);

		when(interviewDetailRepository.findById(interviewQuestion.getId()))
				.thenReturn(Optional.of(interviewDetailFirst));

		InterviewQuestion nextQuestion = interviewService.processAnswerAndGetNextQuestion(session, interviewQuestion);

		assertThat(nextQuestion).isNotNull();
		assertThat(nextQuestion.getQuestion()).isEqualTo("질문2");

		//processAnswerAndGetNextQuestion메서드가 실행될때
		//내부의 getNextQuestion이 실행되어 index가 1증가
		assertThat(session.getAttribute("currentIndex")).isEqualTo(2);
	}

	@DisplayName("다음 질문 반환")
	@Test
	void testGetNextQuestion() {
		List<InterviewDetail> questions = new ArrayList<>();

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		InterviewDetail interviewDetail1 = InterviewDetail.createInterviewDetail("질문1?", Category.LANGUAGE, REAL, LocalDateTime.now());
		questions.add(interviewDetail);
		questions.add(interviewDetail1);

		session = new MockHttpSession();
		session.setAttribute("questions", questions);
		session.setAttribute("currentIndex", 0);

		nextQuestion = interviewService.getNextQuestion(session);

		assertThat(nextQuestion).isNotNull();
		assertThat(nextQuestion.getQuestion()).isEqualTo("질문?");

		nextQuestion = interviewService.getNextQuestion(session);

		assertThat(nextQuestion).isNotNull();
		assertThat(nextQuestion.getQuestion()).isEqualTo("질문1?");

		nextQuestion = interviewService.getNextQuestion(session);
		assertThat(nextQuestion).isNull();

	}

//	@DisplayName("마지막 질문")
//	@Test
//	void testLastQuestion() {
//
//		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, PRACTICE);
//		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, PRACTICE, LocalDateTime.now());
//
//		interviewDetailList.add(interviewDetail);
//
//		interviewQuestion = InterviewQuestion.builder()
//			.id(1L)
//			.question(interviewDetail.getQuestion())
//			.size(1)
//			.build();
//
//		session = new MockHttpSession();
//
//		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));
//		when(interviewDetailRepository.findById(interviewQuestion.getId())).thenReturn(Optional.of(interviewDetail));
//
//		String result = interviewService.lastQuestion(interviewQuestion, session);
//
//		assertEquals("success", result);
//	}

	@DisplayName("모의면접 결과 요약")
	@Test
	void testSummaryInterview() {

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		interviewDetail.setAnswer("답변");
		interviewDetailList.add(interviewDetail);

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, REAL);
		interview.addInterviewDetails(interviewDetail);

		session = new MockHttpSession();

		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

		List<InterviewResultDetail> result = interviewService.summaryInterview(session);

		assertThat(result).isNotEmpty();
		assertThat(result.get(0).getQuestion()).isEqualTo("질문?");
		assertThat(result.get(0).getAnswer()).isEqualTo("답변");
	}

	@DisplayName("모의면접 상세 조회")
	@Test
	void testGetInterviewDetailsById() {

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		interviewDetail.setAnswer("답변");
		interviewDetailList.add(interviewDetail);

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, REAL);
		interview.addInterviewDetails(interviewDetail);

		session = new MockHttpSession();

		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

		List<InterviewResultDetail> result = interviewService.getInterviewDetailsById(interview.getId());

		assertThat(result).isNotEmpty();
		assertThat(result.get(0).getQuestion()).isEqualTo("질문?");
		assertThat(result.get(0).getAnswer()).isEqualTo("답변");
	}


}
