package com.est.jobshin.domain.interviewDetail.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
//@Transactional
@RequiredArgsConstructor
public class InterviewDetailService {
    private final InterviewDetailRepository interviewDetailRepository;
    private final AlanService alenService;

    //면접 실전모드로 진입시
    //질문 카테고리 랜덤하게 생성
    @Transactional
    public void realModeStarter(Interview interview, User user) {
        Category[] categories = selectCategories(5);
        createInterviewDetail(interview, categories, user);
    }

    //면접 연습모드로 진입시
    //사용자가 선택한 카테고리로 질문 카테고리 생성
    @Transactional
    public void practiceModeStarter(Interview interview, Category category, User user) {
        Category[] categories = new Category[5];
        Arrays.fill(categories, category);
        createInterviewDetail(interview, categories, user);
    }

    //면접 질문 생성, db에 저장
    @Transactional
    public void createInterviewDetail(Interview interview, Category[] category, User user) {
        String questionData = alenService.callAlan(category, user.getLanguage(), user.getPosition(), user.getLevel());

        ArrayList<String> questionList = new ArrayList<>();

        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(questionData);

        while (matcher.find()) {
            questionList.add(matcher.group(1).substring(matcher.group(1).indexOf(':')+1).trim());
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

    //사용자의 답변으로 부터 피드백 생성, db에 저장
    @Transactional
    public void getAnswerByUser(InterviewQuestion interviewQuestion) {
        InterviewDetail interviewDetail = interviewDetailRepository.findById(interviewQuestion.getId())
                .orElseThrow(RuntimeException::new);
        interviewDetail.registerAnswer(interviewQuestion.getAnswer());

        String feedback = alenService.callAnswer(interviewQuestion.getQuestion(), interviewQuestion.getAnswer());

        ArrayList<String> feedbackList = new ArrayList<>();

        String regex = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(feedback);

        while (matcher.find()) {
            feedbackList.add(matcher.group(1));
        }

        interviewDetail.registerFeedback(feedbackList.get(1), Long.parseLong(feedbackList.get(0)));
    }

//    @Transactional(readOnly = true)
//    public List<InterviewQuestion> getInterviewDetailByInterviewId(Long interviewId){
//        return interviewDetailRepository
//                .findByInterviewId(interviewId)
//                .stream()
//                .map(InterviewQuestion::fromInterviewDetail)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public InterviewDetail getInterviewDetailById(Long interviewDetailId) {
//        return interviewDetailRepository
//                .findById(interviewDetailId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid interview details id: " + interviewDetailId));
//    }

    //랜덤한 카테고리 배열 생성
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
