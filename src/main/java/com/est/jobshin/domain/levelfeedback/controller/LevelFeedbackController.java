package com.est.jobshin.domain.levelfeedback.controller;

import com.est.jobshin.domain.levelfeedback.dto.LevelFeedbackDto;
import com.est.jobshin.domain.levelfeedback.service.LevelFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class LevelFeedbackController {

    private final LevelFeedbackService feedbackService;

    // 피드백 제출
//    @PostMapping
//    public ResponseEntity<LevelFeedbackDto> submitFeedback(@RequestBody @Valid LevelFeedbackDto feedbackDto) {
//        feedbackService.saveFeedback(feedbackDto);
//        return ResponseEntity.ok(feedbackDto);
//    }
}