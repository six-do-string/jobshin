package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private LocalDateTime createAt;

    private UserDto user;

    private List<InterviewQuestion> interviewDetails;

    public static InterviewDto fromInterview(Interview interview) {
        InterviewDto interviewDto;
        interviewDto = InterviewDto.builder()
                .title(interview.getTitle())
                .createAt(interview.getCreateAt())
                .build();
        interviewDto.setInterviewDetails(new ArrayList<>());
        return interviewDto;
    }

    public Interview toInterview() {
        return Interview.builder()
                .title(title)
                .createAt(createAt)
                .build();
    }
}
