package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock-interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;
    private final AlanService alanService;

    @GetMapping("/test")
    public String callAlan() {
        String result = alanService.callAlan();

        return result;
    }

    @PostMapping
    public ResponseEntity<Interview> createInterview(@RequestBody InterviewDto interviewDto) {
        return ResponseEntity.ok(interviewService.createInterview(interviewDto));
    }

//    @GetMapping
//    public ResponseEntity<List<InterviewsDto>> getAllInterviews() {
//        return ResponseEntity.ok(INTERVIEWS_SERVICE.getAllInterviews());
//    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<InterviewDto> getInterviewById(@PathVariable("interviewId") Long interviewId) {
        return ResponseEntity.ok(interviewService.getInterviewById(interviewId));
//                .orElseThrow(() -> new IllegalArgumentException("해당 id는 없는 id 입니다.")));
    }

    @DeleteMapping("/{interviewId}")
    public ResponseEntity<Void> deleteInterview(@PathVariable("interviewId") Long interviewId) {
        interviewService.deleteInterviewsById(interviewId);
        return ResponseEntity.noContent().build();
    }
}
