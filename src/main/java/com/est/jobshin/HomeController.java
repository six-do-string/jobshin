package com.est.jobshin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 메인 폼
    @GetMapping("/views/main")
    public String mainPage(Model model) {
        model.addAttribute("siteName", "JOB SHIN");
        model.addAttribute("heroMessage", "AI가 질문을 하고 피드백까지 제공하는 모의면접을 체험하라!");
        model.addAttribute("featureDescription1",
                "JOB SHIN은 앨런 이라는 AI 챗봇을 기반으로 탄생한 모의 기술 면접 서비스 입니다. "
                        + "AI가 면접관으로서 질문을 하고 여러분들로부터 받은 답변을 평가합니다. 질문은 회원가입 때 작성하였던 포지션, 주언어 정보를 기반으로 생성됩니다.");
        model.addAttribute("realModeDescription",
                "실전 모드는 질문이 분야(CS, 언어) 별로 골고루 생성되며, 총 5문제로 구성됩니다.\n"
                        + "실전 모드는 여러분들의 현재 레벨을 체크하거나 현재 레벨에 따른 난이도의 질문에 대비할 때 유용합니다.");
        model.addAttribute("practiceModeDescription", "연습 모드는 특정 분야에 대한 질문이 생성됩니다. 문제 개수는 실전 모드와 동일하게 총 5문제로 구성됩니다.\n"
                + "연습 모드는 상대적으로 약한 분야에 대한 질문에 대비할 때 유용합니다.");
        model.addAttribute("feedbackDescription",
                "JOB SHIN은 면접이 종료된 후, 여러분들로부터 받은 답변을 토대로 면접 점수와, 모범답변, 그리고 답변에 대한 피드백을 생성합니다. 이는 실제 면접 상황에서 질문에 대해 제시할 답변을 구상할 때 방향성을 제공할 것입니다.");

        return "layout/main";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/views/main";
    }
}
