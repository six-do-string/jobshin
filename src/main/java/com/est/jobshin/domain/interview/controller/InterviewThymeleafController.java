package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;

@Controller
@RequestMapping("/views")
public class InterviewThymeleafController {

    @GetMapping("/interviewMainPage")
    public String getInterview(HttpSession session) {
        return "interview/interviewMainPage";
    }

    @GetMapping("/mode")
    public String getInterviewReal(@RequestParam Mode mode) {
        if (mode == REAL) {
            return "interview/interviewRealEnter";
        } else if (mode == PRACTICE) {
            return "interview/interviewPracticeEnter";
        } else {
            return "error";
        }
    }

    @GetMapping("/question/real")
    public String startInterviewReal() {
        return "interview/interviewQuestion";
    }

    @GetMapping("/question/practice")
    public String startInterviewPractice() {
        return "interview/interviewQuestion";
    }

    @GetMapping("/interview/result")
    public String getInterviewResult() {
        return "AlFeedback";
    }

    @GetMapping("/interview/levelEval")
    public String getInterviewLevelEval() {
        return "levelFeedback";
    }

}
