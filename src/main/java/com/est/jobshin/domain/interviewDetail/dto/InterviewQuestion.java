package com.est.jobshin.domain.interviewDetail.dto;

import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/** DTO for {@link com.est.jobshin.domain.interviewDetail.domain.InterviewDetail} */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewQuestion {
    private Long id;
    private Long interviewId;
    private String question;
    private String answer;
    private InterviewDetail.Category category;
    private InterviewDetail.Mode mode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private InterviewDto interview;

    public InterviewDetail toInterviewDetail() {
        return InterviewDetail.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .category(category)
                .mode(mode)
                .createdAt(createdAt)
                .build();
    }

    public static InterviewQuestion fromInterviewDetail(InterviewDetail interviewDetail) {
        return InterviewQuestion.builder()
                .id(interviewDetail.getId())
                .interviewId(interviewDetail.getInterview().getId())
                .question(interviewDetail.getQuestion())
                .answer(interviewDetail.getAnswer())
                .category(interviewDetail.getCategory())
                .mode(interviewDetail.getMode())
                .createdAt(interviewDetail.getCreatedAt())
                .build();
    }
}
