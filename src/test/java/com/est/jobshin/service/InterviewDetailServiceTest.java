package com.est.jobshin.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import com.est.jobshin.infra.alan.AlanService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class InterviewDetailServiceTest {

	@Mock
	InterviewDetailRepository interviewDetailRepository;

	@Mock
	AlanService alanService;

	@InjectMocks
	InterviewDetailService interviewDetailService;

	@DisplayName("모의면접 상세 생성")
	@Test
	void createInterviewDetailTest(){

	    //Given
		User user = User.builder()
			.username("test@test.com")
			.password("password")
			.nickname("테스트")
			.language(Language.JAVA)
			.position(Position.BACKEND)
			.build();

		Interview interview = Interview.builder()
			.title("모의면접 테스트")
			.mode(REAL)
			.interviewDetails(new ArrayList<>())
			.createdAt(LocalDateTime.now())
			.build();

		Category[] categories = {Category.CS, Category.LANGUAGE, Category.LANGUAGE, Category.ALGORITHM, Category.ALGORITHM};

		String mockQuestionData = "[Q1: 질문1] [Q2: 질문2] [Q3: 질문3] [Q4: 질문4] [Q5: 질문5]";

		when(alanService.callAlan(categories, user.getLanguage(), user.getPosition(), user.getLevel())).thenReturn(mockQuestionData);
		when(interviewDetailRepository.save(any(InterviewDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

		//When
		interviewDetailService.createInterviewDetail(interview, categories, user);

	    //Then
		assertThat(interview.getInterviewDetails()).hasSize(5);
		assertThat(interview.getInterviewDetails().get(0).getQuestion()).isEqualTo("질문1");
		verify(interviewDetailRepository, times(5)).save(any(InterviewDetail.class));

	}

	@DisplayName("사용자의 답변 저장 및 AI의 평가 생성")
	@Test
	void getAnswerAndCheckAnswerTest(){

		//Given
		InterviewDetail interviewDetail = InterviewDetail.builder()
			.id(1L)
			.question("질문")
			.answer(null)
			.category(Category.LANGUAGE)
			.mode(REAL)
			.score(null)
			.createdAt(LocalDateTime.now())
			.build();

		InterviewQuestion interviewQuestion = InterviewQuestion.builder()
			.id(interviewDetail.getId())
			.question("질문")
			.answer("답변")
			.build();

		String mockFeedback = "[90][평가내용]";

		when(interviewDetailRepository.findById(interviewDetail.getId())).thenReturn(Optional.of(interviewDetail));
		when(alanService.callAnswer(interviewQuestion.getQuestion(), interviewQuestion.getAnswer())).thenReturn(mockFeedback);

		//When
		interviewDetailService.getAnswerByUser(interviewQuestion);

	    //Then
		assertThat(interviewDetail.getAnswer()).isEqualTo("답변");
		assertThat(interviewDetail.getScore()).isEqualTo(90L);
		verify(interviewDetailRepository, times(1)).findById(interviewDetail.getId());


	}

}
