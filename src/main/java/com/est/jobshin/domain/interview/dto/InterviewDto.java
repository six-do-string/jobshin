package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewDto {
    private String title;

    private LocalDateTime createdAt;

    private Mode mode;

    private List<InterviewDetailDto> interviewDetailDto;

    public static InterviewDto fromInterview(Interview interview) {
        List<InterviewDetailDto> interviewDetailDto = new ArrayList<>();
        if(interview.getInterviewDetails() != null) {
            interviewDetailDto = interview.getInterviewDetails().stream()
                    .map(InterviewDetailDto::from)
                    .toList();
        }
        return InterviewDto.builder()
                .title(interview.getTitle())
                .createdAt(interview.getCreatedAt())
                .mode(interview.getMode())
                .interviewDetailDto(interviewDetailDto)
                .build();
    }
}
