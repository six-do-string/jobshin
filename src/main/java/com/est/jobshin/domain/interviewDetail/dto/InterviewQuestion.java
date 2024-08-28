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

///** DTO for {@link com.est.jobshin.domain.interviewDetail.domain.InterviewDetail} */
@Data
@Builder
public class InterviewQuestion implements Serializable {

    private Long id;

    private Long interviewId;

    private String question;

    private String answer;

    private InterviewDetail.Category category;

    private InterviewDetail.Mode mode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

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

    public InterviewDetail toInterviewDetail() {
        InterviewDetail interviewDetail = new InterviewDetail();
        interviewDetail.setId(id);
        interviewDetail.setCategory(category);
        interviewDetail.setMode(mode);
        interviewDetail.setCreatedAt(createdAt);
        interviewDetail.setQuestion(question);
        return interviewDetail;
    }
}
