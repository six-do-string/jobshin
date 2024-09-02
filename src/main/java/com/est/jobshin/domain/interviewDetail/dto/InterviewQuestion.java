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
public class InterviewQuestion {
    private Long id;
    private String question;
    private String answer;

    public static InterviewQuestion from(InterviewDetail interviewDetail) {
        return InterviewQuestion.builder()
                .id(interviewDetail.getId())
                .question(interviewDetail.getQuestion())
                .build();
    }
}
