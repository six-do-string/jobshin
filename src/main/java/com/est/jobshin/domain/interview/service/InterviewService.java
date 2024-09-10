package com.est.jobshin.domain.interview.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewDto;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.dto.InterviewResultDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.service.InterviewDetailService;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewService {

    private static final String PRACTICE_MODE = " 파트 연습 모드";
    private static final String REAL_MODE = "실전 모드";

    private final InterviewRepository interviewRepository;
    private final InterviewDetailService interviewDetailService;
    private final UserRepository userRepository;

    private final Map<String, ConcurrentLinkedQueue<InterviewQuestion>> answerQueueMap = new HashMap<>();
    private final Map<String, Boolean> taskStateMap = new HashMap<>();
    private final InterviewDetailRepository interviewDetailRepository;

    /**
     * 면접 id로 면접 찾기
     * @param id 찾을 면접 id
     * @return 면접 정보를 담은 DTO
     */
    @Transactional
    public InterviewDto getInterviewById(Long id) {
        return interviewRepository
                .findById(id)
                .map(InterviewDto::fromInterview)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));
    }

    /**
     * 세션데이터 초기화
     * @param questions 질문과 답변이 들어있는 객체
     * @param interviewId 진행중인 면접 id
     * @param session 현재 세션
     */
    private void initSessionData(List<InterviewDetail> questions, Long interviewId, HttpSession session) {
        session.setAttribute("questions", new ArrayList<>(questions));
        session.setAttribute("currentIndex", 0);
        session.setAttribute("interviewId", interviewId);

        answerQueueMap.put(session.getId(), new ConcurrentLinkedQueue<>());
        taskStateMap.put(session.getId(), false);
    }

    /**
     * 면접 이어하기시 세션데이터 초기화
     * @param questions 질문과 답변이 들어있는 객체
     * @param interviewId 진행중인 면접 id
     * @param session 현재 세션
     */
    private void initSessionDataAtLoadIncompleteInterview(List<InterviewDetail> questions, Long interviewId, HttpSession session) {
        session.setAttribute("questions", new ArrayList<>(questions));
        session.setAttribute("currentIndex", 0);
        session.setAttribute("interviewId", interviewId);

        if(!taskStateMap.getOrDefault(session.getId(), false)) {
            answerQueueMap.put(session.getId(), new ConcurrentLinkedQueue<>());
            taskStateMap.put(session.getId(), false);
        }
    }

    /**
     * 면접 연습모드로 진입시 면접 질문 생성 메서드 호출,
     * 면접 진행에 필요한 변수들을 세션에 초기화
     * @param category 클라이언트가 선택한 카테고리
     * @param session 현재 세션
     * @return 면접 질문이 저장된 Interview
     */
    @Transactional
    public Interview createPracticeInterview(Category category, HttpSession session) {
        User user = getCurrentUser();
        Interview interview = Interview.createInterview(
            category.name() + PRACTICE_MODE, LocalDateTime.now(), user, Mode.PRACTICE
        );

        Interview createdInterview = interviewRepository.save(interview);

        interviewDetailService.practiceModeStarter(interview, category, user);

        initSessionData(interview.getInterviewDetails(), createdInterview.getId(), session);

        return createdInterview;
    }

    /**
     * 면접 실전모드로 진입시 면접 질문 생성 메서드 호출,
     * 면접 진행에 필요한 변수들을 세션에 초기화
     * @param session 현재 세션
     * @return 면접 질문이 저장된 Interview
     */
    @Transactional
    public Interview createRealInterview(HttpSession session) {
        User user = getCurrentUser();
        Interview interview = Interview.createInterview(
                REAL_MODE, LocalDateTime.now(), user, Mode.REAL
        );

        Interview createdInterview = interviewRepository.save(interview);

        interviewDetailService.realModeStarter(interview, user);

        initSessionData(interview.getInterviewDetails(), createdInterview.getId(), session);

        return createdInterview;
    }

    /**
     * 면접 이어하기 진입시 미완료된 문항 추출,
     * 면접 진행에 필요한 변수들을 세션에 초기화
     * @param interviewId 이어서 진행할 면접의 id
     * @param session 현재 세션
     * @return 면접 질문이 저장된 Interview
     */
    @Transactional
    public Interview loadIncompleteInterview(Long interviewId, HttpSession session) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new NoSuchElementException("Interview not found"));

        List<InterviewDetail> questions = interview.getInterviewDetails().stream()
                .filter(interviewDetail -> !interviewDetail.isComplete())
                .collect(Collectors.toList());

        initSessionDataAtLoadIncompleteInterview(questions, interviewId, session);

        return interview;
    }

    /**
     * 답변을 전달받으면 다음 질문을 전달,
     * 답변은 큐에 저장하고 답변 처리 메서드가 중단 상태면 실행
     * @param session 현재 세션
     * @param interviewQuestion 클라이언트로부터 작성된 답변을 담은 객체
     * @return 다음 질문이 담긴 객체
     */
    @Transactional
    public InterviewQuestion processAnswerAndGetNextQuestion(HttpSession session, InterviewQuestion interviewQuestion) {
        InterviewQuestion nextQuestion = getNextQuestion(session);

        InterviewDetail interviewDetail = interviewDetailRepository.findById(interviewQuestion.getId())
                        .orElseThrow(() -> new NoSuchElementException("InterviewDetail not found"));

        interviewDetail.registerAnswerComplete();

        CompletableFuture.runAsync(() -> {
            log.info("큐에 저장");
            answerQueueMap.get(session.getId()).add(interviewQuestion);
            if(!taskStateMap.get(session.getId())) {
                taskStateMap.put(session.getId(), true);
                queueTask(answerQueueMap.get(session.getId()), session);
            }
        });

        return nextQuestion;
    }

    /**
     * 답변 처리 메서드 호출, 처리해야 할 작업이 없으면 상태를 중단으로 변경하고 작업 중단
     * @param questions 처리해야 할 답변 처리 작업이 담긴 큐
     * @param session 현재 세션
     */
    private void queueTask(ConcurrentLinkedQueue<InterviewQuestion> questions, HttpSession session) {
        while(!questions.isEmpty()) {
            log.info("큐에서 뺌");
            interviewDetailService.getAnswerByUser(questions.poll());
        }
        taskStateMap.put(session.getId(), false);
    }

    /**
     * 다음 질문 전달
     * @param session 현재 세션
     * @return 다음 질문이 담긴 객체
     */
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

    /**
     * 마지막으로 전달받은 답변에 대해 동기적으로 처리,
     * 앞선 비동기 작업이 완전히 처리 되었는지 재확인
     * @param interviewQuestion InterviewQuestion
     * @param session HttpSession
     * @return 완료처리를 나타내는 문자열
     */
    public String lastQuestion(InterviewQuestion interviewQuestion, HttpSession session) {
        Interview interview = interviewRepository.findById((Long)session.getAttribute("interviewId"))
                .orElseThrow(() -> new NoSuchElementException("Interview not found"));
        interview.completeInterview();

        InterviewDetail interviewDetail = interviewDetailRepository.findById(interviewQuestion.getId())
                .orElseThrow(() -> new NoSuchElementException("Interview not found"));

        interviewDetail.registerAnswerComplete();

        long startTime = System.currentTimeMillis();
        long timeout = 2 * 60 * 1000;

        while(taskStateMap.get(session.getId())) {
            if(System.currentTimeMillis() - startTime > timeout) {
                log.info("세션 {}: 타임아웃", session.getId());
                break;
            }

            log.info("작업 상태: {}", taskStateMap.get(session.getId()));
            log.info("{}: 대기중", session.getId());
            log.info("남은 작업 개수: {}", answerQueueMap.get(session.getId()).size());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(!answerQueueMap.get(session.getId()).isEmpty()) {
            queueTask(answerQueueMap.get(session.getId()), session);
        }

        interviewDetailService.getAnswerByUser(interviewQuestion);

        answerQueueMap.remove(session.getId());
        taskStateMap.remove(session.getId());
        return "success";
    }

    /**
     * 면접 종료시 면접 결과 전달
     * @param session 현재 세션
     * @return 면접 결과와 피드백이 담긴 리스트
     */
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

    /**
     * 면접 기록 삭제
     * @param id 면접 id
     */
    public void deleteInterviewsById(Long id) {
        interviewRepository.deleteById(id);
    }

    /**
     * 현재 세션의 사용자 정보를 가져오는 메서드
     * @return 현재 세션의 사용자 정보
     */
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