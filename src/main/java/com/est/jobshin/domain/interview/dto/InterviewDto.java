package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto2;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewDto {
    private String title;

    private LocalDateTime createAt;

    private UserDto user;

    private Mode mode;

    private List<InterviewDetailDto2> interviewDetailDto2;

    public static InterviewDto fromInterview(Interview interview) {
        List<InterviewDetailDto2> interviewDetailDto2 = new ArrayList<>();
        if(interview.getInterviewDetails() != null) {
            interviewDetailDto2 = interview.getInterviewDetails().stream()
                    .map(InterviewDetailDto2::from)
                    .toList();
        }
        return InterviewDto.builder()
                .title(interview.getTitle())
                .createAt(interview.getCreateAt())
                .mode(interview.getMode())
                .interviewDetailDto2(interviewDetailDto2)
                .build();
    }

    public Interview toInterview() {
        return Interview.builder()
                .title(title)
                .createAt(createAt)
                .build();
    }
}
