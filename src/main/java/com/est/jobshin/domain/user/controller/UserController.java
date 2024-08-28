package com.est.jobshin.domain.user.controller;

import com.est.jobshin.domain.user.dto.request.CreateUserRequest;
import com.est.jobshin.domain.user.dto.request.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.response.UserResponse;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // 회원가입 폼
    @GetMapping("/view/signup")
    public String userSignUpForm(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());

        return "user/signup";
    }

    @GetMapping("/view/edit")
    public String userEditFrom(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "redirect:/view/login";
        }
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = principal.getUsername();
        UserResponse userResponse = userService.findByUsername(username);
        model.addAttribute("updateUserRequest", UpdateUserRequest.toResponseDto(userResponse));

        return "user/edit";
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


    @PutMapping("/user/edit")
    public String userEdit(@Valid UpdateUserRequest updateUserRequest,
            BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 인증되지 않은 사용자인 경우 로그인 페이지로 리디렉션
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "redirect:/view/login";
        }

        // 사용자 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // 유효성 검사 오류가 있는 경우 편집 페이지로 리디렉션
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        // 비밀번호 암호화 및 사용자 정보 업데이트
        updateUserRequest.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
        userService.updateUser(username, updateUserRequest);

        return "redirect:/";
    }

    @GetMapping("/user/logout")
    public String userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy()
                .getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @DeleteMapping("/api/users/{username}")
    @ResponseBody
    public ResponseEntity<Void> userDelete(@PathVariable String username,
            HttpServletRequest request, HttpServletResponse response) {

        // 현재 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 있을 때만 로그아웃 처리
        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        userService.deleteUser(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

