package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;

@Controller
public class InterviewThymeleafController {

    private final InterviewDetailRepository interviewDetailRepository;

    public InterviewThymeleafController(InterviewDetailRepository interviewDetailRepository) {
        this.interviewDetailRepository = interviewDetailRepository;
    }

    @GetMapping("/views/interviewMainPage")
    public String getInterview(HttpSession session) {
        return "interview/interviewMainPage";
    }

    @GetMapping("/views/mode")
    public String getInterviewReal(@RequestParam Mode mode, HttpSession session) {
        if (mode == REAL) {
            return "interview/interviewRealEnter";
        } else if (mode == PRACTICE) {
            return "interview/interviewPracticeEnter";
        } else {
            return "error";
        }
    }

    @GetMapping("/views/question")
    public String startInterview(Model model) {
        return "interview/interviewQuestion";
    }
}
