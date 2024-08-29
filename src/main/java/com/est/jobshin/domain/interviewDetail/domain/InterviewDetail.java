package com.est.jobshin.domain.interviewDetail.domain;

import com.est.jobshin.domain.interview.domain.Interview;

import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "interview_details")
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 1000)
    private String question;

    private String answer;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    private Long score;

    private String exampleAnswer;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;

    private InterviewDetail(String question, Category category, Mode mode, LocalDateTime createdAt) {
        this.question = question;
        this.category = category;
        this.mode = mode;
        this.createdAt = createdAt;
    }

    public static InterviewDetail createInterviewDetail(String question, Category category, Mode mode, LocalDateTime createdAt) {
        return new InterviewDetail(question, category, mode, createdAt);
    }
}
