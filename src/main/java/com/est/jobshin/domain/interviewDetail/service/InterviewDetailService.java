package com.est.jobshin.domain.interviewDetail.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.infra.alan.AlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class InterviewDetailService {
    private final InterviewDetailRepository interviewDetailRepository;
    private final AlanService alenService;
    private final InterviewRepository interviewRepository;

    @Autowired
    public InterviewDetailService(InterviewDetailRepository interviewDetailRepository, AlanService alenService, InterviewRepository interviewRepository) {
        this.interviewDetailRepository = interviewDetailRepository;
        this.alenService = alenService;
        this.interviewRepository = interviewRepository;
    }

    public InterviewQuestion createInterviewDetail(InterviewQuestion interviewQuestion, Long interviewId) {
        Interview interview = interviewRepository
                .findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
        String questionData = alenService.callAlan();

        if (questionData.length() > 255) {
            questionData = questionData.substring(0, 255);
        }

        InterviewDetail interviewDetail = interviewQuestion.toInterviewDetail();
        interviewDetail.setCreatedAt(LocalDateTime.now());
        interviewDetail.setInterview(interview);
        interviewDetail.setQuestion(questionData);

        InterviewDetail savedInterviewDetail = interviewDetailRepository.save(interviewDetail);

        interview.addInterviewDetails(savedInterviewDetail);

        return InterviewQuestion.fromInterviewDetail(savedInterviewDetail);
    }

    @Transactional(readOnly = true)
    public List<InterviewQuestion> getInterviewDetailByInterviewId(Long interviewId){
        return interviewDetailRepository
                .findByInterviewId(interviewId)
                .stream()
                .map(InterviewQuestion::fromInterviewDetail)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterviewQuestion getInterviewDetailById(Long interviewDetailId) {
        return interviewDetailRepository
                .findById(interviewDetailId)
                .map(InterviewQuestion::fromInterviewDetail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview details id: " + interviewDetailId));
    }

}
