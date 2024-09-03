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
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping("/api/mock-interviews/practice")
    public ResponseEntity<InterviewDto> createPracticeInterview(@RequestParam Category category, HttpSession session) {
        Interview interview = interviewService.createPracticeInterview(category, session);
        return ResponseEntity.ok(InterviewDto.fromInterview(interview));
    }

    @PostMapping("/api/mock-interviews/real")
    public ResponseEntity<InterviewDto> createRealInterview(HttpSession session) {
        Interview interview = interviewService.createRealInterview(session);
        return ResponseEntity.ok(InterviewDto.fromInterview(interview));
    }

    @GetMapping("/api/mock-interviews/next")
    public ResponseEntity<InterviewQuestion> next(HttpSession session) {
        InterviewQuestion question = interviewService.getNextQuestion2(session);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/api/mock-interviews/next")
    public ResponseEntity<InterviewQuestion> next2(@RequestBody InterviewQuestion interviewQuestion, HttpSession session) {
        InterviewQuestion question = interviewService.processAnswerAndGetNextQuestion(session, interviewQuestion);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/api/mock-interviews/finish")
    public ResponseEntity<String> finish(@RequestBody InterviewQuestion interviewQuestion) {
        String string = interviewService.lastQuestion(interviewQuestion);
        return ResponseEntity.ok(string);
    }

    @GetMapping("/api/mock-interviews/summary")
    public ResponseEntity<List<InterviewResultDetail>> summary(HttpSession session) {
        List<InterviewResultDetail> interviewResultDetails = interviewService.summaryInterview(session);
        return ResponseEntity.ok(interviewResultDetails);
    }

    @GetMapping("/api/mock-interviews/{interviewId}")
    public ResponseEntity<InterviewDto> getInterviewById(@PathVariable("interviewId") Long interviewId) {
        return ResponseEntity.ok(interviewService.getInterviewById(interviewId));
    }

    @DeleteMapping("/api/mock-interviews/{interviewId}")
    public ResponseEntity<Void> deleteInterview(@PathVariable("interviewId") Long interviewId) {
        interviewService.deleteInterviewsById(interviewId);
        return ResponseEntity.noContent().build();
    }
}
