package com.est.jobshin.infra.alan;

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
		String content = String.format(PromptMessage.DEFAULT_PROMPT, level);
		String requestUrl = String.format("%s?content=%s&client_id=%s", apiUrl, content, clientId);
		log.info("Calling API: {}", requestUrl);
		String response = restTemplate.getForObject(requestUrl, String.class);
		log.info("API response received");
		log.info("Response: {}", response);
		return response;
	}
}
