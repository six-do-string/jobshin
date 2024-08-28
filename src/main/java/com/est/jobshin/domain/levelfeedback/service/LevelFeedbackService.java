package com.est.jobshin.domain.levelfeedback.service;


import com.est.jobshin.domain.levelfeedback.domain.LevelFeedback;
import com.est.jobshin.domain.levelfeedback.dto.LevelFeedbackDto;
import com.est.jobshin.domain.levelfeedback.repository.LevelFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelFeedbackService {

    private final LevelFeedbackRepository feedbackRepository;

    // 피드백 저장
    public LevelFeedback saveFeedback(LevelFeedbackDto feedbackDto) {
        LevelFeedback feedback = new LevelFeedback();
        feedback.setUserId(feedbackDto.getUserId());
        feedback.setContent(feedbackDto.getContent());
        feedback.setRating(feedbackDto.getRating());

        return feedbackRepository.save(feedback);
    }

    // 특정 사용자 ID로 피드백 조회
    public List<LevelFeedback> getFeedbacksByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }

    // 피드백 ID로 피드백 조회
    public Optional<LevelFeedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
}