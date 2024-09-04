package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.dto.InterviewResultDetail;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final InterviewDetailService interviewDetailService;
    private final UserRepository userRepository;
    private final AnswerProcessingService answerProcessingService;

    @Transactional
    public InterviewDto getInterviewById(Long id) {
        return interviewRepository
                .findById(id)
                .map(InterviewDto::fromInterview)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
    }

    //면접 연습모드로 진입시
    //면접 생성 메서드 호출
    //세션에 면접 진행에 필요한 변수들 초기화
    @Transactional
    public Interview createPracticeInterview(Category category, HttpSession session) {
        User user = getCurrentUser();
        Interview interview = Interview.createInterview(
                null, LocalDateTime.now(), user, Mode.PRACTICE
        );

        Interview createdInterview = interviewRepository.save(interview);

        interviewDetailService.practiceModeStarter(interview, category, user);

        session.setAttribute("questions", new ArrayList<>(interview.getInterviewDetails()));
        session.setAttribute("currentIndex", 0);

        session.setAttribute("interviewId", createdInterview.getId());

        return createdInterview;
    }

    //면접 실전모드로 진입시
    //면접 생성 메서드 호출
    //세션에 면접 진행에 필요한 변수들 초기화
    @Transactional
    public Interview createRealInterview(HttpSession session) {
        User user = getCurrentUser();
        Interview interview = Interview.createInterview(
                null, LocalDateTime.now(), user, Mode.REAL
        );

        Interview createdInterview = interviewRepository.save(interview);

        interviewDetailService.realModeStarter(interview, user);

        session.setAttribute("questions", new ArrayList<>(interview.getInterviewDetails()));
        session.setAttribute("currentIndex", 0);

        session.setAttribute("interviewId", createdInterview.getId());

        return createdInterview;
    }

    @Transactional
    public Interview loadIncompleteInterview(Long interviewId, HttpSession session) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NoSuchElementException("Interview not found"));

        List<InterviewDetail> questions = interview.getInterviewDetails().stream()
                .filter(interviewDetail -> !interviewDetail.isComplete())
                .collect(Collectors.toList());

        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);

        session.setAttribute("interviewId", interviewId);

        return interview;
    }

    //답변이 들어왔을때 다음 질문을 반환하고, 답변에 대한 처리는 비동기적으로 처리
    @Transactional
    public InterviewQuestion processAnswerAndGetNextQuestion(HttpSession session, InterviewQuestion interviewQuestion) {
        InterviewQuestion nextQuestion = getNextQuestion(session);

        Long interviewId = (Long) session.getAttribute("interviewId");

        answerProcessingService.addAnswer(interviewId, interviewQuestion);

//        CompletableFuture.runAsync(() -> {
//            interviewDetailService.getAnswerByUser(interviewQuestion);
//        });

        return nextQuestion;
    }

    //다음 질문 반환
    public InterviewQuestion getNextQuestion(HttpSession session) {
        List<InterviewDetail> questions = (List<InterviewDetail>) session.getAttribute("questions");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        if (questions == null || currentIndex == null || currentIndex >= questions.size()) {
            return null;
        }

        InterviewDetail question = questions.get(currentIndex);
        session.setAttribute("currentIndex", currentIndex + 1);

        return InterviewQuestion.from(question, questions.size());
    }

    @Transactional
    public String lastQuestion(InterviewQuestion interviewQuestion, HttpSession session) {
        Interview interview = interviewRepository.findById((Long)session.getAttribute("interviewId"))
                .orElseThrow(() -> new NoSuchElementException("Interview not found"));
        interview.completeInterview();
//        interviewDetailService.getAnswerByUser(interviewQuestion);

        //
        Long interviewId = (Long) session.getAttribute("interviewId");
        List<InterviewDetail> questions = (List<InterviewDetail>) session.getAttribute("questions");
        int size = questions.size();
        answerProcessingService.addAnswerAndReturnStatus(interviewId, interviewQuestion, size);
        return "success";
    }

    public List<InterviewResultDetail> summaryInterview(HttpSession session) {
        return getInterviewDetailsById((Long) session.getAttribute("interviewId"));
    }

    public List<InterviewResultDetail> getInterviewDetailsById(Long interviewId) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NoSuchElementException("Interview not found"));

        List<InterviewDetail> interviewDetails = interview.getInterviewDetails();

        return interviewDetails.stream()
                .map(InterviewResultDetail::from)
                .collect(Collectors.toList());
    }

    public void deleteInterviewsById(Long id) {
        interviewRepository.deleteById(id);
    }

    //현재 세션의 사용자 정보 가져오기
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new RuntimeException();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}