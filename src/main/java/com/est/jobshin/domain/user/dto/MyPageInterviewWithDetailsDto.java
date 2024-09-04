package com.est.jobshin.domain.user.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
public class MyPageInterviewWithDetailsDto {

	private Long id;
	private String title;
	private Mode mode;
	private LocalDateTime createdAt;
	private Long userId;
	private List<MyPageInterviewDetailDto> interviewDetails;
	private int averageScore;

	public static MyPageInterviewWithDetailsDto fromInterview(Interview interview) {
		List<MyPageInterviewDetailDto> interviewDetailDtos = interview.getInterviewDetails()
			.stream()
			.map(MyPageInterviewDetailDto::fromInterviewDetail)
			.collect(Collectors.toList());

		return MyPageInterviewWithDetailsDto.builder()
			.id(interview.getId())
			.title(interview.getTitle())
			.mode(interview.getMode())
			.createdAt(interview.getCreatedAt())
			.userId(interview.getUser().getId())
			.interviewDetails(interviewDetailDtos)
			.build();
	}
}
