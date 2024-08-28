package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final AlanService alanService;

    @Transactional(readOnly = true)
    public InterviewDto getInterviewById(Long id) {
        return interviewRepository
                .findById(id)
                .map(InterviewDto::fromInterview)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
    }

//        @Transactional(readOnly = true)
//        public List<InterviewsDto> getAllInterviews() {
//            return INTERVIEWS_REPOSITORY.findAll()
//                    .stream().map(InterviewsDto::fromInterviews)
//                    .collect(Collectors.toList());
//        }

    public Interview createInterview(InterviewDto interviewDto) {
//        Interview interview = interviewDto.toInterview();
//        interview.setInterviewDetails(new ArrayList<>());
//        interview.setCreateAt(LocalDateTime.now());
//        Interview savedInterview = interviewRepository.save(interview);

//        User user = getCurrentUser();
        Interview interview = Interview.createInterview(
                interviewDto.getTitle(), LocalDateTime.now());

        return interviewRepository.save(interview);
    }

    public void deleteInterviewsById(Long id) {
        interviewRepository.deleteById(id);
    }

//    private String getAlanData() {
//        String questionData = alanService.callAlan();
//
//        if (questionData.length() > 255) {
//            questionData = questionData.substring(0, 255);
//        }
//
//        return questionData;
//    }

//    private User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
//            throw new RuntimeException();
//        }
//
//        UserResponse principal = (UserResponse) authentication.getPrincipal();
//        return userRepository.findByemail(principal.getEmail())
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//    }
}