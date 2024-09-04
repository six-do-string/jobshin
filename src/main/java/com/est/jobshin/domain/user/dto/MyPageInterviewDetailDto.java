package com.est.jobshin.domain.user.dto;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageInterviewDetailDto {

	private Long id;
	private String question;
	private String answer;
	private Category category;
	private Mode mode;
	private double score;
	private String exampleAnswer;
	private LocalDateTime createdAt;

	public static MyPageInterviewDetailDto fromInterviewDetail(InterviewDetail interviewDetail) {
		return MyPageInterviewDetailDto.builder()
			.id(interviewDetail.getId())
			.question(interviewDetail.getQuestion())
			.answer(interviewDetail.getAnswer())
			.category(interviewDetail.getCategory())
			.mode(interviewDetail.getMode())
			.score(interviewDetail.getScore() != null ? interviewDetail.getScore() : 0L)
			.exampleAnswer(interviewDetail.getExampleAnswer())
			.createdAt(interviewDetail.getCreatedAt())
			.build();
	}
}
