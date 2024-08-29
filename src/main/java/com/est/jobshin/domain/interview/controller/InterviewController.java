package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.service.InterviewResultService;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.infra.alan.AlanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mock-interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;
    private final InterviewResultService interviewResultService;
    private final AlanService alanService;

    @GetMapping("/test")
    public String callAlan() {
        String result = alanService.callRealMode();

        return result;
    }
//    세션 테스트용 임시 주석처리
//    @PostMapping
//    public ResponseEntity<InterviewDto> createInterview(@RequestBody InterviewDto interviewDto, HttpSession session) {
//        Interview interview = interviewService.createInterview(interviewDto, session);
//        return ResponseEntity.ok(InterviewDto.fromInterview(interview));
//    }

    //세션 테스트용
    @GetMapping
    public ResponseEntity<InterviewDto> createInterview(HttpSession session) {
        InterviewDto interviewDto = new InterviewDto();
        Interview interview = interviewService.createInterview(interviewDto, session);
        return ResponseEntity.ok(InterviewDto.fromInterview(interview));
    }

    @GetMapping("/next")
    public ResponseEntity<String> next(HttpSession session) {
        String question = interviewService.getNextQuestion2(session);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/{interviewDetailId}/userAnswer")
    public ResponseEntity<InterviewDetail> saveAnswer(@PathVariable("interviewDetailId") Long interviewDetailId, String userAnswer) {
        interviewResultService.saveAnswer(interviewDetailId, "userAnswer");
        InterviewDetail interviewDetail = interviewResultService.createExampleAnswer(interviewDetailId, "userAnswer");
        return ResponseEntity.ok(interviewDetail);
    }

    @GetMapping("/{interviewId}/{interviewDetailId}")
    public ResponseEntity<InterviewDetail> getInterviewDetail(@PathVariable("interviewId") Long interviewId, @PathVariable("interviewDetailId") Long interviewDetailId) {
        return ResponseEntity.ok(interviewResultService.getInterviewDetail(interviewDetailId));
    }

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
