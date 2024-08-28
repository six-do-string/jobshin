package com.est.jobshin.infra.alan;

import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.util.Language;
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

	@Value("${alan.api.default-url}")
	private String defaultUrl;

	@Value("${alan.api.client-id}")
	private String clientId;

	public String callAlan() {
		return callApi(defaultUrl, clientId, "test");
	}


	private String callApi(String apiUrl, String clientId, String level) {
		String content = String.format(PromptMessage.QUESTION_PROMPT, level);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);
		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);
		return response;
	}

	// 실전 모드 문제
	private String callApiRealMode(String apiUrl, String clientId, String level, User user) {
		String setUpMessage = user.getId() + "전체 레벨(구성 레벨: LV1, LV2, LV3) 중" + user.getLevel() + "사용 언어: " + user.getLanguage();

		String questionMessage = PromptMessage.QUESTION_PROMPT + setUpMessage;

		String content = String.format(questionMessage, level);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);
		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);
		return response;
	}

	// 연습 모드 문제
	private String callApiPracticeMode(String apiUrl, String ClientId, String level, User user){
		String setUpMessage = user.getId() + "전체 레벨(구성 레벨: LV1, LV2, LV3) 중" + user.getLevel() + "사용언어: " + user.getLanguage();

		String questionMessage = "";

//		if (InterviewDetail.Category.CS) {
//			questionMessage =
//					PromptMessage.QUESTION_PRACTICE_CS_PROMPT + setUpMessage;
//		} else if(InterviewDetail.Category.LANGUAGE){
//			questionMessage =
//					PromptMessage.QUESTION_PRACTICE_LANGUAGE_PROMPT + setUpMessage;
//		} else if(InterviewDetail.Category.ALGORITHM) {
//			questionMessage =
//					PromptMessage.QUESTION_PRACTICE_ALGORITHM_PROMPT + setUpMessage;
//		}

		String content = String.format(questionMessage, level);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);

		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);

		return response;
	}

}
