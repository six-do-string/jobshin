package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.infra.alan.AlanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final InterviewDetailService interviewDetailService;
//    private Iterator<InterviewDetail> interviewDetailIterator;

    @Transactional
    public InterviewDto getInterviewById(Long id) {
        return interviewRepository
                .findById(id)
                .map(InterviewDto::fromInterview)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
    }

    @Transactional
    public Interview createInterview(InterviewDto interviewDto, HttpSession session) {
        Interview interview = Interview.createInterview(
                interviewDto.getTitle(), LocalDateTime.now());

        Interview createdInterview = interviewRepository.save(interview);

        interviewDetailService.createInterviewDetail(interview);

//        interviewDetailIterator = interview.getInterviewDetails().iterator();

        //메시지큐에 넣기
//        for(InterviewDetail interviewDetail : interview.getInterviewDetails()) {
//            messageQueueService.enqueueQuestion(interviewDetail.getId(), interviewDetail.getQuestion());
//        }
//        System.out.println();
        session.setAttribute("questions", new ArrayList<>(interview.getInterviewDetails()));
        session.setAttribute("currentIndex", 0);

        return createdInterview;
    }

    public String getNextQuestion2(HttpSession session) {
        List<InterviewDetail> questions = (List<InterviewDetail>) session.getAttribute("questions");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        if (questions == null || currentIndex == null || currentIndex >= questions.size()) {
            return "No more questions";
        }

        InterviewDetail question = questions.get(currentIndex);
        session.setAttribute("currentIndex", currentIndex + 1);

        return question.getQuestion();
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