package com.est.jobshin.domain.interviewDetail.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewQuestion;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.infra.alan.AlanService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterviewDetailService {

    private final InterviewDetailRepository interviewDetailRepository;
    private final AlanService alenService;

    private static final Integer NUMBER_OF_SELECT = 5;
    private static final String REGEX = "\\[(.*?)\\]";

    /**
     * 면접 실전모드로 진입시 질문 카테고리 배열을 랜덤하게 생성,
     * 면접 질문 생성 메서드 호출
     * @param interview 현재 진행중인 면접
     * @param user 현재 세션의 유저
     */
    @Transactional
    public void realModeStarter(Interview interview, User user) {
        Category[] categories = selectCategories();
        createInterviewDetail(interview, categories, user);
    }

    /**
     * 면접 연습모드로 진입시 클라이언트가 선택한 카테고리로 배열을 생성,
     * 면접 질문 생성 메서드 호출
     * @param interview 현재 진행중인 면접
     * @param category 클라이언트가 선택한 카테고리
     * @param user 현재 세션의 유저
     */
    @Transactional
    public void practiceModeStarter(Interview interview, Category category, User user) {
        Category[] categories = new Category[NUMBER_OF_SELECT];
        Arrays.fill(categories, category);
        createInterviewDetail(interview, categories, user);
    }

    /**
     * 면접 질문을 생성하고, DB에 저장
     * @param interview 현재 진행중인 면접
     * @param category 생성할 문제의 카테고리 배열
     * @param user 현재 세션의 유저
     */
    @Transactional
    public void createInterviewDetail(Interview interview, Category[] category, User user) {
        String questionData = alenService.callAlan(category, user.getLanguage(), user.getPosition(), user.getLevel());

        ArrayList<String> questionList = new ArrayList<>();

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(questionData);

        while (matcher.find()) {
            questionList.add(matcher.group(1).substring(matcher.group(1).indexOf(':')+1).trim());
        }

        for(int i = 0; i < NUMBER_OF_SELECT; i++){
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

    /**
     * 사용자의 답변으로 부터 피드백을 생성, DB에 저장
     * @param interviewQuestion 질문과 사용자가 작성한 답변이 담긴 객체
     */
    @Transactional
    public void getAnswerByUser(InterviewQuestion interviewQuestion) {
        InterviewDetail interviewDetail = interviewDetailRepository.findById(interviewQuestion.getId())
                .orElseThrow(RuntimeException::new);
        interviewDetail.registerAnswer(interviewQuestion.getAnswer());

        String feedback = alenService.callAnswer(interviewQuestion.getQuestion(), interviewQuestion.getAnswer());

        ArrayList<String> feedbackList = new ArrayList<>();

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(feedback);

        while (matcher.find()) {
            feedbackList.add(matcher.group(1));
        }

        interviewDetail.registerFeedback(feedbackList.get(1), Long.parseLong(feedbackList.get(0)));
    }

    /**
     * 랜덤한 카테고리 배열 생성
     * @return 생성된 카테고리 배열
     */
    private Category[] selectCategories() {
        Random random = new Random();
        Category[] categories = Category.values();
        Category[] selectedCategories = new Category[NUMBER_OF_SELECT];
        for(int i = 0; i < NUMBER_OF_SELECT; i ++){
            int randomIndex = random.nextInt(categories.length);
            selectedCategories[i] = categories[randomIndex];
        }
        return selectedCategories;
    }
}
