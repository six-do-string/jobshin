package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewAnswer;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.levelfeedback.domain.LevelFeedback;
import com.est.jobshin.domain.levelfeedback.service.LevelFeedbackService;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewResultService {
    private final InterviewDetailRepository interviewDetailRepository;
    private final AlanService alenService;
    private final InterviewRepository interviewRepository;
    private final InterviewDetailService interviewDetailService;
    private final LevelFeedbackService levelFeedbackService;

    public InterviewAnswer createInterviewAnswer(
            InterviewAnswer interviewAnswer, Long interviewId, Long interviewDetailId) {

        Interview interview =
                interviewRepository
                        .findById(interviewId)
                        .orElseThrow(() -> new IllegalArgumentException("Interview not found"));

        List<InterviewDetail> questions = interview.getInterviewDetails();

        String feedbackData = alenService.callFeedback();
        Long score = Long.parseLong(feedbackData.split("\n")[0]);

        String exampleAnswer = alenService.callAnswer();

//        interviewAnswer.setExampleAnswer(exampleAnswer);
//        interviewAnswer.setScore(score);

        InterviewDetail interviewDetail =
                interviewDetailService
                        .getInterviewDetailById(interviewDetailId)
                        .toInterviewDetail();
        interviewDetail.setExampleAnswer(exampleAnswer);
        interviewDetail.setScore(score);

        InterviewDetail savedInterviewDetail = interviewDetailRepository.save(interviewDetail);

        return InterviewAnswer.fromInterviewDetail(savedInterviewDetail);
    }

    private Long getFeedbackScore (Long feedbackId) {
        LevelFeedback feedback = levelFeedbackService.getFeedbackById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found FeedbackId"));
        return feedback.getRating();
    }

    public void updateUserLevel(User user, Long feedbackId, InterviewDetail interviewDetail){
        Long score = interviewDetail.getScore();
        Long result = (getFeedbackScore(feedbackId) + score) / 2;

        Enum<Level> level = user.getLevel();

        if (result < 40) {
            level = Level.LV1;

        } else if(result < 60) {
            level = Level.LV2;

        } else {
            level = Level.LV3;
        }
    }
}
