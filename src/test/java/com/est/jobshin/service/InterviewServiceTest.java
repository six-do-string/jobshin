package com.est.jobshin.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

	@Mock
	private InterviewRepository interviewRepository;

	@InjectMocks
	private InterviewService interviewService;

	@DisplayName("면접 조회")
	@Test
	void checkGetInterview(){
		//Given
		Interview interview = Interview.builder()
			.title("모의면접 테스트")
			.mode(REAL)
			.createdAt(LocalDateTime.now())
			.build();

		InterviewDetail interviewDetail = InterviewDetail.builder()
			.question("질문?")
			.answer("답변")
			.category(Category.LANGUAGE)
			.exampleAnswer("예시 답안")
			.mode(REAL)
			.score(99L)
			.createdAt(LocalDateTime.now())
			.interview(interview)
			.build();

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
	void checkDeleteInterview(){

	    //Given
		Interview interview = Interview.builder()
			.title("모의면접 테스트")
			.mode(REAL)
			.createdAt(LocalDateTime.now())
			.build();

	    //When
		interviewService.deleteInterviewsById(interview.getId());

	    //Then
		verify(interviewRepository, times(1)).deleteById(interview.getId());
	}

//	@DisplayName("모의면접 테스트")
//	@Test
//	@WithUserDetails(value = "Test@test.com")
//	void createRealInterview() throws Exception{
//
//		//Given
//		User user = User.builder()
//			.username("Test@test.com")
//			.password("password")
//			.nickname("테스트")
//			.language(JAVA)
//			.position(Position.BACKEND)
//			.build();
//
//		userRepository.save(user);
//
//		Interview interview = new Interview();
//
//		MockHttpSession session = new MockHttpSession();
//
//		session.setAttribute("user", user);
//
////		SecurityContext context = SecurityContextHolder.getContext();
////		context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword()));
//
//		//When
//
//		Interview createdRealInterview = interviewService.createRealInterview(session);
//
//		//Then
//
//		assertThat(createdRealInterview).isNotNull();
//
//	}


}
