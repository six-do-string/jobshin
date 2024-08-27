package com.est.jobshin.domain.user.controller;

import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    // 로그인 폼
    @GetMapping("/view/login")
    public String userLoginForm() {
        return "user/login";
    }
    // 메인 폼
    @GetMapping("/view/main")
    public String mainPage(){
        return "layout/main";
    }

    // 회원가입 폼
    @GetMapping("/view/signup")
    public String userSignUpForm(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());

        return "user/signup";
    }

    @PostMapping("/view/signup")
    public String userSignUp(@Valid CreateUserRequest createUserRequest,
            BindingResult bindingResult) {
        // 아이디 중복 검사
        if (!userService.isDuplicate(createUserRequest.getUsername())) {
            bindingResult.addError(
                    new FieldError("createUserRequest", "username", "이미 존재하는 회원입니다."));
            return "user/signup";
        }

        if (bindingResult.hasErrors()) {
            return "user/signup";
        }

        createUserRequest.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userService.createUser(createUserRequest);

        return "redirect:/";
    }
}

