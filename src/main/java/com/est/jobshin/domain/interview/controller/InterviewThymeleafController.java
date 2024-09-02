package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewResultDetail;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;

@Controller
public class InterviewThymeleafController {
    private final InterviewDetailService interviewDetailService;
    private final InterviewService interviewService;

    public InterviewThymeleafController(InterviewDetailService interviewDetailService, InterviewService interviewService) {
        this.interviewDetailService = interviewDetailService;
        this.interviewService = interviewService;
    }

    @GetMapping("/views/interviewMainPage")
    public String getInterview(HttpSession session) {
        return "interview/interviewMainPage";
    }

    @GetMapping("/views/mode")
    public String getInterviewReal(@RequestParam Mode mode) {
        if (mode == REAL) {
            return "interview/interviewRealEnter";
        } else if (mode == PRACTICE) {
            return "interview/interviewPracticeEnter";
        } else {
            return "error";
        }
    }

    @GetMapping("/views/question/real")
    public String startInterviewReal() {
        return "interview/interviewQuestion";
    }

    @GetMapping("/views/question/practice")
    public String startInterviewPractice() {
        return "interview/interviewQuestion";
    }

    @GetMapping("/views/interview/result")
    public String getInterviewResult() {
//        List<InterviewResultDetail> interviewDetail = interviewService.getInterviewDetails(id);
//        interviewDetail.forEach(interviewResultDetail -> {
//            String feedback = interviewResultDetail.getExampleAnswer();
//            model.addAttribute("exampleAnswer", feedback);
//        });

        return "interviewFeedback";
    }

    @GetMapping("/views/interviews/practice")
    public String interviewPracticeHistoryForm() {
        return "interview/interviewPracticeHistory";
    }

    @GetMapping("/views/interviews/real")
    public String interviewPracticeRealForm() {
        return "interview/interviewRealHistory";
    }
}
