package com.est.jobshin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // 메인 폼
    @GetMapping("/view/main")
    public String mainPage(Model model) {
        model.addAttribute("siteName", "JOB SHIN");
        model.addAttribute("heroMessage", "AI가 질문을 하고 피드백까지 제공하는 모의면접을 체험하라!");
        model.addAttribute("featureDescription1", "JOB SHIN은 '챗봇'이라는 AI 챗봇을 기반으로 한 모의 기술 면접 서비스입니다...");
        model.addAttribute("practiceModeDescription", "면접 전 긴장감을 높이고 싶다면? 실전 문제 풀이로 실제와 유사한 면접 환경을 체험해보세요...");
        model.addAttribute("realModeDescription", "면접 전 충분한 연습이 필요하다면? 연습 모드로 여유롭게 준비하세요...");
        model.addAttribute("feedbackDescription", "JOB SHIN은 면접이 종료된 후, 여러분들로부터 받은 답변 피드백을 면접 유형별, 문항별로 제공합니다...");

        return "layout/main";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/view/main";
    }
}
