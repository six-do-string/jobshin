package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;

@Controller
public class InterviewThymeleafController {
    private final InterviewDetailService interviewDetailService;

    public InterviewThymeleafController(InterviewService interviewService, InterviewDetailService interviewDetailService) {
        this.interviewDetailService = interviewDetailService;
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
    public String startInterviewReal(Model model) {
        return "interview/interviewQuestion";
    }

    @GetMapping("/views/question/practice")
    public String startInterviewPractice() {
        return "interview/interviewQuestion";
    }

    @GetMapping("/views/interview/result")
    public String getInterviewResult() {
        return "AlFeedback";
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
