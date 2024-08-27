package com.est.jobshin.domain.interviewDetail.controller;

import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock-interviews/detail")
@RequiredArgsConstructor
public class InterviewDetailController {
    private final InterviewDetailService interviewDetailService;

    @PostMapping("/{interviewId}")
    public ResponseEntity<InterviewQuestion> addInterviewDetails(@PathVariable("interviewId") Long interviewId, @RequestBody InterviewQuestion interviewQuestion) {
        interviewDetailService.createInterviewDetail(interviewQuestion, interviewId);
        return ResponseEntity.ok(interviewQuestion);
    }

    @GetMapping("/question/{interviewId}/{detailId}")
    public ResponseEntity<InterviewDetailDto> getInterviewDetailById(@PathVariable("interviewId") Long interviewId,@PathVariable("detailId") Long detailId) {
        return ResponseEntity.ok(interviewDetailService.getInterviewDetailById(detailId));
    }

    @GetMapping("question/{interviewId}")
    public ResponseEntity<List<InterviewDetailDto>> getInterviewDetailByInterviewId(@PathVariable("interviewId") Long interviewId) {
        return ResponseEntity.ok(interviewDetailService.getInterviewDetailByInterviewId(interviewId));
    }
}
