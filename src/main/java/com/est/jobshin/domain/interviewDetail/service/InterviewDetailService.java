package com.est.jobshin.domain.interviewDetail.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
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
//@Transactional
@RequiredArgsConstructor
public class InterviewDetailService {
    private final InterviewDetailRepository interviewDetailRepository;
    private final AlanService alenService;
    private final InterviewRepository interviewRepository;

    @Transactional
    public void createInterviewDetail(Interview interview) {
        //카테고리 선별 구현
        //임시로 구현
        Category[] category = {Category.CS, Category.CS, Category.CS, Category.CS, Category.CS};

        //callAlan 에 추가해야 할 파라미터
        //1. 카테고리
        String questionData = alenService.callAlan();
//        String questionData = alenService.callRealMode();
//        String questionData = alenService.callPracticeMode();

        //데이터 처리
        ArrayList<String> questionList = new ArrayList<>();

        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(questionData);

        while (matcher.find()) {
            questionList.add(matcher.group(1));
        }

        //임시데이터
//        questionList.add("질문");
//        questionList.add("질문");
//        questionList.add("질문");
//        questionList.add("질문");
//        questionList.add("질문");

        //위 과정으로 나온 데이터는 질문 리스트 5개
        //category 랑 맵핑해서 db에 저장
        //interview 랑 연결

        for(int i = 0; i < 5; i++){
            InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail(
                    questionList.get(i),
                    category[i],
                    interview.getMode(),
                    LocalDateTime.now()
            );
            InterviewDetail savedInterviewDetail = interviewDetailRepository.save(interviewDetail);
            interview.addInterviewDetails(savedInterviewDetail);
        }
    }

    // 사용자 답변 저장
    public void saveAnswer(Long interviewDetailId, String userAnswer) {
        InterviewDetail interviewDetail =
                interviewDetailRepository
                        .findById(interviewDetailId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found InterviewDetail"));
        if (interviewDetail.getQuestion() != null) {
            interviewDetail.setAnswer(userAnswer);
        }
        interviewDetailRepository.save(interviewDetail);
    }

    // 예시 답안 생성
    @Transactional
    public InterviewDetail createExampleAnswer(Long interviewDetailId, String userAnswer) {
        InterviewDetail interviewDetail =
                interviewDetailRepository
                        .findById(interviewDetailId)
                        .orElseThrow(() -> new IllegalArgumentException("Not Found InterviewDetail"));

//        saveAnswer(interviewDetailId, userAnswer);

        if (interviewDetail.getAnswer() != null) {
            String message = interviewDetail.getAnswer()+"에 대해";
            String exampleAnswer = message + alenService.callAnswer();
//            String exampleAnswer = alenService.callAnswer();
            interviewDetail.setExampleAnswer(exampleAnswer);
        }

        return interviewDetailRepository.save(interviewDetail);
    }

//    @Transactional(readOnly = true)
//    public List<InterviewQuestion> getInterviewDetailByInterviewId(Long interviewId){
//        return interviewDetailRepository
//                .findByInterviewId(interviewId)
//                .stream()
//                .map(InterviewQuestion::fromInterviewDetail)
//                .collect(Collectors.toList());
//    }

//    @Transactional(readOnly = true)
//    public InterviewDetail getInterviewDetailById(Long interviewDetailId) {
//        return interviewDetailRepository
//                .findById(interviewDetailId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid interview details id: " + interviewDetailId));
//    }

}
