package com.est.jobshin.domain.interviewDetail.dto;

import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class InterviewDetailDto implements Serializable {
    private Long id;
    private Long interviewId;
    private String question;
    private String answer;
    private InterviewDetail.Category category;
    private InterviewDetail.Mode mode;
    private Long score;
    private String exampleAnswer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
//    private InterviewDto interview;

    public static InterviewDetailDto fromInterviewDetail(InterviewDetail interviewDetail) {
        InterviewDto interviewDto = InterviewDto.fromInterview(interviewDetail.getInterview());
        return InterviewDetailDto.builder()
                .id(interviewDetail.getId())
                .interviewId(interviewDetail.getInterview().getId())
                .question(interviewDetail.getQuestion())
                .answer(interviewDetail.getAnswer())
                .category(interviewDetail.getCategory())
                .mode(interviewDetail.getMode())
                .score(interviewDetail.getScore())
                .exampleAnswer(interviewDetail.getExampleAnswer())
                .createdAt(interviewDetail.getCreatedAt())
                .build();
    }
}
