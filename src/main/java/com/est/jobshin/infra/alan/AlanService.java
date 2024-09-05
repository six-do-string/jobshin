package com.est.jobshin.infra.alan;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class AlanService {

	private final RestTemplate restTemplate;

	private Interview interview;

	private InterviewDetail interviewDetail;

	private User user;

	@Value("${alan.api.default-url}")
	private String defaultUrl;

	@Value("${alan.api.client-id}")
	private String clientId;

	public String callRealMode() {
		return callApiRealMode(defaultUrl, clientId, interview);
	}

	public String callPracticeMode() {
		return callApiPracticeMode(defaultUrl, clientId, user, interviewDetail);
	}

	public String callAnswer(String question, String answer) {
		return callApiAnswer(defaultUrl, clientId, question, answer);
	}

	public String callAlan(Category[] categories, Language language, Position position, Level level) {
		return callApi(defaultUrl, clientId, categories, language, position, level);
	}


	private String callApi(String apiUrl, String clientId, Category[] categories, Language language, Position position, Level level) {
		String message = position + PromptMessage.QUESTION_PROMPT + "전체 레벨(구성 레벨: LV1, LV2, LV3) 중 " + level + " 사용 언어: " + language + "출제 카테고리는 1~5번까지 각각 " + Arrays.toString(categories);
		String content = String.format(message);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);
		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);
		return response;
	}

	// 실전 모드 문제
	private String callApiRealMode(String apiUrl, String clientId, Interview interview) {
		String setUpMessage
				= "전체 레벨(구성 레벨: LV1, LV2, LV3) 중" + interview.getUser().getLevel() + "사용 언어: " + interview.getUser().getLanguage();

		String questionMessage = PromptMessage.QUESTION_PROMPT + setUpMessage;

		String content = String.format(questionMessage);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);
		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);
		return response;
	}

	// 연습 모드 문제
	private String callApiPracticeMode(String apiUrl, String ClientId, User user, InterviewDetail interviewDetail){
		String setUpMessage
				= "전체 레벨(구성 레벨: LV1, LV2, LV3) 중" + user.getLevel() + "사용언어: " + user.getLanguage() + "선택한 카테고리: " + interviewDetail.getCategory();

		String questionMessage = "3가지 카테고리(CS(컴퓨터 과학 기초), 프로그래밍 언어 및 도구, 알고리즘) 중 선택한 카테고리 내에서만"
				+ PromptMessage.QUESTION_PROMPT + setUpMessage;

		String content = String.format(questionMessage);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);

		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);

		return response;
	}

	// 모범답안
	private String callApiAnswer(String apiUrl, String ClientId, String question, String answer) {
		String message = question + " 라는 질문에 대한 답변이야. " + answer + ". " + PromptMessage.ANSWER_PROMPT;
		String content = String.format(message);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);

		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);

		return response;
	}
}
