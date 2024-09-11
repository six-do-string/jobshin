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

	@Value("${alan.api.default-url}")
	private String defaultUrl;

	@Value("${alan.api.client-id}")
	private String clientId;

	public String callAnswer(String question, String answer) {
		return callApiAnswer(question, answer);
	}

	public String callAlan(Category[] categories, Language language, Position position, Level level) {
		return callApi(categories, language, position, level);
	}

	/**
	 * 면접 질문 생성 요청
	 * @param categories 면접 질문 카테고리
	 * @param language User에서 가져온 주 사용 언어
	 * @param position User에서 가져온 지원 포지션
	 * @param level User에서 가져온 레벨
	 * @return 면접 질문 5개
	 */
	private String callApi(Category[] categories, Language language, Position position, Level level) {
		String message = position + PromptMessage.QUESTION_PROMPT + "전체 레벨(구성 레벨: LV1, LV2, LV3) 중 " + level + " 사용 언어: " + language + "출제 카테고리는 1~5번까지 각각 " + Arrays.toString(categories) + PromptMessage.FORMAT_PROMPT;
		String content = String.format(message);
		String requestUrl = String.format("%s?content=%s&client_id=%s", defaultUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);
		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);
		return response;
	}

	/**
	 * 답변에 대한 피드백 요청
	 * @param question 면접 질문
	 * @param answer 사용자가 작성한 답변
	 * @return 답변에 대한 점수와 예시답변
	 */
	private String callApiAnswer(String question, String answer) {
		String message = question + " 라는 면접 질문에 대한 답변이야. " + answer + ". " + PromptMessage.ANSWER_PROMPT;
		String content = String.format(message);
		String requestUrl = String.format("%s?content=%s&client_id=%s", defaultUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);

		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);

		return response;
	}
}
