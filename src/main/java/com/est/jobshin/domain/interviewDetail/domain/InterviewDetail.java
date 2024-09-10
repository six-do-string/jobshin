package com.est.jobshin.domain.interviewDetail.domain;

import com.est.jobshin.domain.interview.domain.Interview;

import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "interview_details")
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String question;

    @Column(length = 2000)
    private String answer;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Mode mode;

    private Long score = 0L;

    @Column(length = 2000)
    private String exampleAnswer;

    private boolean complete = false;

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

    /**
     * 전달 받은 답변을 저장
     * @param answer 클라이언트가 전달한 답변
     */
    public void registerAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * 답변 등록 상태로 변경
     */
    public void registerAnswerComplete() {
        this.complete = true;
    }

    /**
     * 전달 받은 피드백을 저장, 해당 문제를 완료처리
     * @param exampleAnswer 앨런으로부터 전달 받은 예시 답변
     * @param score 앨런으로부터 전달 받은 점수
     */
    public void registerFeedback(String exampleAnswer, Long score) {
        this.exampleAnswer = exampleAnswer;
        this.score = score;
        this.complete = true;
    }
}
