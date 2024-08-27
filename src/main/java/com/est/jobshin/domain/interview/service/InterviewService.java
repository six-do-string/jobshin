package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Transactional
public class InterviewService {
    private final InterviewRepository interviewRepository;

    @Autowired
    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    @Transactional(readOnly = true)
    public InterviewDto getInterviewById(Long id) {
        return interviewRepository
                .findById(id)
                .map(InterviewDto::fromInterview)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
    }

    //    @Transactional(readOnly = true)
    //    public List<InterviewsDto> getAllInterviews() {
    //        return INTERVIEWS_REPOSITORY.findAll()
    //                .stream().map(InterviewsDto::fromInterviews)
    //                .collect(Collectors.toList());
    //    }

    public InterviewDto createInterview(InterviewDto interviewDto) {
        Interview interview = interviewDto.toInterview();
        interview.setInterviewDetails(new ArrayList<>());
        interview.setCreateAt(LocalDateTime.now());
        Interview savedInterview = interviewRepository.save(interview);

        return InterviewDto.fromInterview(savedInterview);
    }

    public void deleteInterviewsById(Long id) {
        interviewRepository.deleteById(id);
    }
}