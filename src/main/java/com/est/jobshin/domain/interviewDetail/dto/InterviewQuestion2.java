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
public class InterviewQuestion2 {
    private Long id;
    private String question;
    private String answer;

    public static InterviewQuestion2 from(InterviewDetail interviewDetail) {
        return InterviewQuestion2.builder()
                .id(interviewDetail.getId())
                .question(interviewDetail.getQuestion())
                .build();
    }
}
