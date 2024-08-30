package com.est.jobshin.domain.interviewDetail.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion2;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
    public void realModeStarter(Interview interview) {
        Category[] categories = selectCategories(5);
        createInterviewDetail(interview, categories);
    }

    @Transactional
    public void practiceModeStarter(Interview interview, Category category) {
        Category[] categories = new Category[5];
        Arrays.fill(categories, category);
        createInterviewDetail(interview, categories);
    }

    @Transactional
    public void createInterviewDetail(Interview interview, Category[] category) {
        //카테고리 선별 구현
        //임시로 구현
//        Category[] category = {Category.CS, Category.CS, Category.CS, Category.CS, Category.CS};

        //callAlan 에 추가해야 할 파라미터
        //1. 카테고리
        String questionData = alenService.callAlan();

        //데이터 처리
        ArrayList<String> questionList = new ArrayList<>();

        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(questionData);

        while (matcher.find()) {
            questionList.add(matcher.group(1));
        }

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

    @Transactional
    public void getAnswerByUser(InterviewQuestion2 interviewQuestion2) {
        InterviewDetail interviewDetail = interviewDetailRepository.findById(interviewQuestion2.getId())
                .orElseThrow(RuntimeException::new);

        interviewDetail.registerAnswer(interviewQuestion2.getAnswer());

        //엘런 서비스로 interviewQuestion2.getAnswer() 을 보내고
        //앨런에게 해당 답에 대한 평가와 예시답안을 받아와서
        String feedback = alenService.callAnswer(interviewQuestion2.getAnswer());

        ArrayList<String> feedbackList = new ArrayList<>();

        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(feedback);

        while (matcher.find()) {
            feedbackList.add(matcher.group(1));
        }

        //db에 저장
        interviewDetail.registerFeedback(feedbackList.get(1), Long.parseLong(feedbackList.get(0)));
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
    public InterviewDetail getInterviewDetailById(Long interviewDetailId) {
        return interviewDetailRepository
                .findById(interviewDetailId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview details id: " + interviewDetailId));
    }

    private Category[] selectCategories(int numberOfSelect) {
        Random random = new Random();
        Category[] categories = Category.values();
        Category[] selectedCategories = new Category[numberOfSelect];
        for(int i = 0; i < numberOfSelect; i ++){
            int randomIndex = random.nextInt(categories.length);
            selectedCategories[i] = categories[randomIndex];
        }
        return selectedCategories;
    }
}
