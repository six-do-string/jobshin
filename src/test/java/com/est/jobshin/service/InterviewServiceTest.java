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
import com.est.jobshin.domain.interviewDetail.util.Mode;
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
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;
import static org.assertj.core.api.Assertions.*;
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
	private InterviewResultDetail interviewResultDetail;
	private List<InterviewResultDetail> interviewResultDetailList = new ArrayList<>();

	@AfterEach
	void tearDown(){
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

		interviewService.createPracticeInterview(interviewDetail.getCategory(),session);

		verify(interviewDetailService, times(1)).practiceModeStarter(any(), any(), any());

	}

	@DisplayName("모의면접 실전모드 생성")
	@Test
	void testCreateRealInterview(){

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
	void testLoadIncompleteInterview(){

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		interviewDetailList.add(interviewDetail);

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, REAL);
		interview.addInterviewDetails(interviewDetail);

		interviewDetailList.add(interviewDetail);

		session = new MockHttpSession();

		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

		interviewService.loadIncompleteInterview(interview.getId(),session);

		assertThat(interviewDetailList).hasSize(1);
		assertThat(interviewDetailList.get(0)).isEqualTo(interviewDetail);
	}

	@DisplayName("모의면접 진행하기")
	@Test
	void testProcessAnswerAndGetNextQuestion(){

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());

		interviewQuestion = InterviewQuestion.builder()
			.id(1L)
			.question(interviewDetail.getQuestion())
			.size(1)
			.build();

		session = new MockHttpSession();

		interviewService.processAnswerAndGetNextQuestion(session,interviewQuestion);

		//미완성
	}

	@DisplayName("다음 질문 반환")
	@Test
	void testGetNextQuestion(){

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());

		interviewQuestion = InterviewQuestion.builder()
			.id(1L)
			.question(interviewDetail.getQuestion())
			.size(1)
			.build();

		session = new MockHttpSession();

		interviewService.getNextQuestion(session);

		//미완성


	}

	@DisplayName("마지막 질문")
	@Test
	void testLastQuestion(){

	}

	@DisplayName("모의면접 결과 요약")
	@Test
	void testSummaryInterview(){

	}

	@DisplayName("모의면접 상세 조회")
	@Test
	void testGetInterviewDetailsById(){

		interviewDetail = InterviewDetail.createInterviewDetail("질문?", Category.LANGUAGE, REAL, LocalDateTime.now());
		interviewDetailList.add(interviewDetail);

		interview = Interview.createInterview("모의면접 테스트", LocalDateTime.now(), user, REAL);
		interview.addInterviewDetails(interviewDetail);

		session = new MockHttpSession();

		when(interviewRepository.findById(interview.getId())).thenReturn(Optional.of(interview));

		interviewService.getInterviewDetailsById(interview.getId());

		//미완성

	}

//	@DisplayName("유저 레벨 업데이트")
//	@Test
//	void testUpdateUserLevel() {
//
//		user = User.builder()
//			.username("test@test.com")
//			.password("password")
//			.nickname("테스트")
//			.language(Language.JAVA)
//			.level(Level.LV2)
//			.position(Position.BACKEND)
//			.build();
//
//		// SecurityContext에 CustomUserDetails 설정
//		CustomUserDetails userDetails = new CustomUserDetails(UserResponse.toDto(user), List.of(new SimpleGrantedAuthority("ROLE_USER")));
//		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(auth);
//
//		Double score = 70D;
//
//		when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
//
//
//
//		interviewService.updateUserLevel(score);
//
//		verify(userRepository, times(1)).save(any(User.class));
//		assertThat(user.getLevel()).isEqualTo(Level.LV3);
//
//	}

}
