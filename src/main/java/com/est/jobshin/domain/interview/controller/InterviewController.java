package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.dto.InterviewResultDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock-interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/practice")
    public ResponseEntity<InterviewDto> createPracticeInterview(@RequestParam Category category, HttpSession session) {
        Interview interview = interviewService.createPracticeInterview(category, session);
        return ResponseEntity.ok(InterviewDto.fromInterview(interview));
    }

    @PostMapping("/real")
    public ResponseEntity<InterviewDto> createRealInterview(HttpSession session) {
        Interview interview = interviewService.createRealInterview(session);
        return ResponseEntity.ok(InterviewDto.fromInterview(interview));
    }

    @GetMapping("/next")
    public ResponseEntity<InterviewQuestion> getNextQuestion(HttpSession session) {
        InterviewQuestion question = interviewService.getNextQuestion(session);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/next")
    public ResponseEntity<InterviewQuestion> submitAnswerAndGetNextQuestion(@RequestBody InterviewQuestion interviewQuestion, HttpSession session) {
        InterviewQuestion question = interviewService.processAnswerAndGetNextQuestion(session, interviewQuestion);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/finish")
    public ResponseEntity<String> finish(@RequestBody InterviewQuestion interviewQuestion) {
        String string = interviewService.lastQuestion(interviewQuestion);
        return ResponseEntity.ok(string);
    }

    @GetMapping("/summary")
    public ResponseEntity<List<InterviewResultDetail>> summary(HttpSession session) {
        List<InterviewResultDetail> interviewResultDetails = interviewService.summaryInterview(session);
        return ResponseEntity.ok(interviewResultDetails);
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> deleteInterview(@PathVariable("interviewId") Long interviewId) {
        interviewService.deleteInterviewsById(interviewId);
        return ResponseEntity.noContent().build();
    }
}
