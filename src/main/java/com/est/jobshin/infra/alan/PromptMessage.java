package com.est.jobshin.infra.alan;

public class PromptMessage {

	public static final String DEFAULT_PROMPT = "자바 백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아줘";

    // 문제 생성 메세지
    public static final String QUESTION_PROMPT =
            "백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아두고 \"한 문제씩만\" 출력해줘. 매 실행마다 다양한 문제들을 출력해주고 문제 레벨은 총 3단계 중 해당 유저의 레벨에 맞춰 뽑아줘. 질문만 출력해주면 돼";

	public static final String ANSWER_PROMPT = "너가 냈던 기술면접 질문 5개에 대한 모범 답안만 알려줘";
	public static final String FEEDBACK_PROMPT = "사용자로부터 받은 답변을 보고 면접관의 입장에서 어떤지 100점 만점에 몇 점인지 평가와 피드백 해줘. 다른 문장 출력할 필요 없어.";

}
