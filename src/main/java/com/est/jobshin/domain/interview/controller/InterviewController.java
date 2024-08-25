package com.est.jobshin.domain.interview.controller;

import com.est.jobshin.infra.alan.AlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InterviewController {

	private final AlanService alanService;

	@GetMapping("/test")
	public String callAlan() {
		String result = alanService.callAlan();

		return result;
	}

}
