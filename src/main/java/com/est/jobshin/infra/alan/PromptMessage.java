package com.est.jobshin.infra.alan;

public class PromptMessage {

	public static final String DEFAULT_PROMPT = "자바 백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아줘";

// 문제 생성 메세지
//	public static final String QUESTION_PROMPT =
//			"백엔드 개발자 취업 준비에 필요한 기술면접 질문을 5개 뽑아두고 \"한 문제씩만\" 출력해줘. 매 실행마다 다양한 문제들을 출력해주고 문제 레벨은 총 3단계 중 해당 유저의 레벨에 맞춰 뽑아줘. 질문만 출력해주면 돼";

	public static final String QUESTION_PROMPT =
			" 개발자 취업 준비에 필요한 기술면접 질문을 5개를 문제 생성마다 최대한 다양하게 출력해줘. 문제 레벨은 총 3단계 중 해당 유저의 레벨에 맞춰 뽑아줘. 주의사항으로는 마크다운 문법을 사용하지마 그리고 형식은 질문외 다른것들은 없애고 질문만 대괄호에 담아서";

//	public static final String ANSWER_PROMPT = "너가 냈던 기술면접 질문 5개에 대한 모범 답안만 알려줘";

	public static final String ANSWER_PROMPT = " 이 답변에 대한 너의 점수를 0~100점으로 평가하고, 예시 답변을 보여줘. 형식은 [점수][예시답변] 이외의 다른 텍스트는 필요없어";

	public static final String FEEDBACK_PROMPT = "사용자로부터 받은 답변을 보고 면접관의 입장에서 점수를 100점 만점에 숫자로만 반환해주고 짧은 피드백 출력해줘. 점수는 맨 위에, 피드백은 밑 줄부터 나오도록하고 다른 문장은 출력하지마.";
	public static final String SCORE_PROMPT = "사용자로부터 받은 답변을 보고 면접관의 입장에서 100점 만점으로 점수를 매겨줘. 숫자로만 반환해";

}
