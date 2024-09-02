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
public class InterviewResultDetail {
    private String question;
    private String answer;
    private String exampleAnswer;
    private Long score;

    public static InterviewResultDetail from(InterviewDetail interviewDetail) {
        return InterviewResultDetail.builder()
                .question(interviewDetail.getQuestion())
                .answer(interviewDetail.getAnswer())
                .exampleAnswer(interviewDetail.getExampleAnswer())
                .score(interviewDetail.getScore())
                .build();
    }
}
