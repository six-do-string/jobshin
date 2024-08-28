package com.est.jobshin.domain.interviewDetail.domain;

import com.est.jobshin.domain.interview.domain.Interview;

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

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "interview_detail")
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(length = 1000)
    private String question;

    @Column
    private String answer;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    public enum Category {
        CS, LANGUAGE, ALGORITHM
    }

    public enum Mode {
        PRACTICE, REAL
    }

    @Column
    private Long score;

    @Column
    private String exampleAnswer;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id")
    private Interview interview;
}
