package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.levelfeedback.domain.LevelFeedback;
import com.est.jobshin.domain.levelfeedback.service.LevelFeedbackService;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewResultService {
    private final InterviewDetailRepository interviewDetailRepository;
    private final AlanService alenService;
    private final LevelFeedbackService levelFeedbackService;
    private final InterviewService interviewService;

    // 사용자 답변 저장
    public void saveAnswer(Long interviewDetailId, String userAnswer) {
        InterviewDetail interviewDetail =
                interviewDetailRepository
                        .findById(interviewDetailId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found InterviewDetail"));
        if (interviewDetail.getQuestion() != null) {
            interviewDetail.setAnswer(userAnswer);
        }
        interviewDetailRepository.save(interviewDetail);
    }

    public InterviewDetail getInterviewDetail(Long interviewDetailId){
        return interviewDetailRepository.findById(interviewDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found InterviewDetail"));
    }

    // 평가 점수
    private Long getRating (Long feedbackId) {
        LevelFeedback feedback = levelFeedbackService.getFeedbackById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found FeedbackId"));
        return feedback.getRating();
    }

    // 예시 답안 생성
    @Transactional
    public InterviewDetail createExampleAnswer(Long interviewDetailId, String userAnswer) {
        InterviewDetail interviewDetail =
                interviewDetailRepository
                        .findById(interviewDetailId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found InterviewDetail"));

//        saveAnswer(interviewDetailId, userAnswer);

        if (interviewDetail.getAnswer() != null) {
            String message = interviewDetail.getAnswer()+"에 대해";
            String exampleAnswer = message + alenService.callAnswer();
            interviewDetail.setExampleAnswer(exampleAnswer);
        }

        return interviewDetailRepository.save(interviewDetail);
    }


}
