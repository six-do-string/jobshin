package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interviewDetail.util.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.interviewDetail.util.Mode.REAL;

@Controller
@RequestMapping("/views")
public class InterviewThymeleafController {

    @GetMapping("/interviewMainPage")
    public String getInterview() {
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

    @GetMapping("/interview/question")
    public String startInterview(@RequestParam("mode") Mode mode){
        return "interview/interviewQuestion";
    }

    @GetMapping("/interview/result")
    public String getInterviewResult(@RequestParam("mode") Mode mode, Model model) {
        if (mode == REAL) {
            model.addAttribute("mode", REAL);
            return "AlFeedback";
        } else if (mode == PRACTICE) {
            model.addAttribute("mode", PRACTICE);
            return "AlFeedback";
        } else {
            return "error";
        }
    }

    @GetMapping("/interview/levelEval")
    public String getInterviewLevelEval() {
        return "levelFeedback";
    }
}