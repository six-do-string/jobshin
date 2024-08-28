package com.est.jobshin.domain.interviewDetail.controller;

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
//    private final InterviewDetailService interviewDetailService;
//
//    @PostMapping("/{interviewId}")
//    public ResponseEntity<InterviewQuestion> createQuestion(@PathVariable("interviewId") Long interviewId, @RequestBody InterviewQuestion interviewQuestion) {
//        InterviewQuestion createdQuestion = interviewDetailService.createInterviewDetail(interviewQuestion, interviewId);
//        return ResponseEntity.ok(createdQuestion);
//    }
//
//    @GetMapping("/question/{interviewId}/{detailId}")
//    public ResponseEntity<InterviewQuestion> getInterviewDetailById(@PathVariable("interviewId") Long interviewId,@PathVariable("detailId") Long detailId) {
//        return ResponseEntity.ok(interviewDetailService.getInterviewDetailById(detailId));
//    }
//
//    @GetMapping("question/{interviewId}")
//    public ResponseEntity<List<InterviewQuestion>> getInterviewDetailByInterviewId(@PathVariable("interviewId") Long interviewId) {
//        return ResponseEntity.ok(interviewDetailService.getInterviewDetailByInterviewId(interviewId));
//    }
}
