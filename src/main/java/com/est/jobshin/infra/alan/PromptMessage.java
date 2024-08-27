package com.est.jobshin.infra.alan;

public class PromptMessage {

	public static final String DEFAULT_PROMPT = "자바 백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아줘";

	// 실전모드 문제 생성 메세지
	public static final String QUESTION_JAVA_PROMPT
			= "자바 백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아주고 하나씩 출력해줘. 매 실행마다 다양한 문제들을 출력해주고 질문 난이도는 총 3개의 레벨(LV1, LV2, LV3) 중 해당 유저의 레벨에 맞춰 뽑아줘. 질문만 출력해주면 돼";
	public static final String QUESTION_PYTHON_PROMPT
			= "파이썬 백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아줘. 질문 5개 중 하나씩, 매 실행마다 다양한 문제들을 출력해주고 질문 난이도는 총 3개의 레벨 중 해당 유저의 레벨에 맞춰 뽑아줘.";

	// 연습모드 문제 생성 메세지
	public static final String QUESTION_PRACTICE_CS_PROMPT = "백엔드 개발자로서 준비해야 하는 CS(컴퓨터 과학 기초)에 대한 면접 질문을 5개 뽑아주고 하나씩 출력해줘. 질문만 뽑아주면 돼";
	public static final String QUESTION_PRACTICE_LANGUAGE_PROMPT = "백엔드 개발자 취업 준비에 필요한 프로그래밍 언어 및 도구와 관련한 기술면접 질문을 5개 뽑아줘. 질문 5개 중 하나씩, 매 실행마다 다양한 문제들을 출력해주고 문제 레벨은 총 3단계 중 해당 유저의 레벨에 맞춰 뽑아줘.";
	public static final String QUESTION_PRACTICE_ALGORITHM_PROMPT = "백엔드 개발자 취업 준비에 필요한 프로그래밍 언어 파이썬과 관련한 기술면접 질문을 5개 뽑아줘. 질문 5개 중 하나씩, 매 실행마다 다양한 문제들을 출력해주고 문제 레벨은 총 3단계 중 해당 유저의 레벨에 맞춰 뽑아줘.";


	public static final String ANSWER_PROMPT = "너가 냈던 기술면접 질문 5개에 대한 모범 답안만 알려줘";
	public static final String FEEDBACK_PROMPT = "사용자로부터 받은 답변을 보고 면접관의 입장에서 어떤지 100점 만점에 몇 점인지 평가와 피드백 해줘. 다른 문장 출력할 필요 없어.";

}
