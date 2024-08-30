package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.infra.alan.AlanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;

@Controller
@RequiredArgsConstructor
public class ThymeleafController {

    private final InterviewService interviewService;
    private final AlanService alanService;

    @GetMapping("/views/interviewMainPage")
    public String getInterview() {
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

    @PostMapping("/views/question/practice")
    public String handleFormSubmission(@RequestParam("category") Category category, Model model) {
        model.addAttribute("selectedCategory", category);
        model.addAttribute("question", new InterviewDetail());
        return "interview/interviewQuestion";
    }

    @GetMapping("/views/question")
    public String startInterview() {

        return "interview/interviewQuestion";
    }
}
