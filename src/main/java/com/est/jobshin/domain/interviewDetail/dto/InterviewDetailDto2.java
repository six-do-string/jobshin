package com.est.jobshin.domain.interviewDetail.dto;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewDetailDto2 {
    private Long id;
    private String question;
    private String answer;

    public static InterviewDetailDto2 from(InterviewDetail interviewDetail) {
        return InterviewDetailDto2.builder()
                .id(interviewDetail.getId())
                .question(interviewDetail.getQuestion())
                .answer(interviewDetail.getAnswer())
                .build();
    }
}
