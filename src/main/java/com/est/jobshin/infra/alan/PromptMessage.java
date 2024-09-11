package com.est.jobshin.infra.alan;

public class PromptMessage {

	public static final String QUESTION_PROMPT =
			" 개발자 취업 준비에 필요한 기술면접 질문을 5개를 문제 생성마다 최대한 다양하게 출력해줘. 문제 레벨은 총 3단계 중 해당 유저의 레벨에 맞춰 뽑아줘. ";

	public static final String FORMAT_PROMPT = ". 형식은 [질문] 이외의 다른 텍스트는 필요없어.";

	public static final String ANSWER_PROMPT = " 이 답변에 대한 너의 점수를 0~100점으로 평가하고, 예시 답변을 보여줘. 형식은 [점수][예시답변] 이외의 다른 텍스트는 필요없어";
}
