package com.est.jobshin.domain.interviewDetail.dto;

import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class InterviewAnswer implements Serializable {
    private Long id;

    private Long interviewId;

    private String question;

    private String answer;

    private Category category;

    private Mode mode;

    private Long score;

    private String exampleAnswer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
//    private InterviewDto interview;

    public InterviewDetail toInterviewDetail() {
        return InterviewDetail.builder()
                .id(id)
                .question(question)
                .answer(answer)
                .category(category)
                .mode(mode)
                .score(score)
                .exampleAnswer(exampleAnswer)
                .build();
    }
}
