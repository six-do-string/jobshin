package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
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
    private final InterviewRepository interviewRepository;
    private final InterviewDetailService interviewDetailService;
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

    // 평가 점수
    private Long getRating (Long feedbackId) {
        LevelFeedback feedback = levelFeedbackService.getFeedbackById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found FeedbackId"));
        return feedback.getRating();
    }

    @Transactional
    public void createExampleAnswer(Interview interview, Long interviewDetailId, String userAnswer) {
        InterviewDetail interviewDetail =
                interviewDetailRepository
                        .findById(interviewDetailId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found InterviewDetail"));

        saveAnswer(interviewDetailId, userAnswer);

        //1. 예시 답변 데이터
        String exampleAnswer = alenService.callAnswer();

        if (interviewDetail.getAnswer() != null) {
            interviewDetail.setExampleAnswer(exampleAnswer);
        }

        //데이터 처리
        ArrayList<String> answerExampleList = new ArrayList<>();

        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(exampleAnswer);

        while (matcher.find()) {
            answerExampleList.add(matcher.group(1));
        }

        for(int i = 0; i < 5; i++){
            interviewDetail.setExampleAnswer(answerExampleList.get(i));
            InterviewDetail savedInterviewDetail = interviewDetailRepository.save(interviewDetail);
            interview.addInterviewDetails(savedInterviewDetail);
        }
    }


}
