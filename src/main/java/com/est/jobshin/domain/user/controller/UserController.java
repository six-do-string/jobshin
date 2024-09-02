package com.est.jobshin.domain.user.controller;

import com.est.jobshin.domain.interview.dto.PracticeInterviewHistorySummaryResponse;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.MyPageInterviewWithDetailsDto;
import com.est.jobshin.domain.user.dto.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.global.security.model.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    // 로그인 폼
    @GetMapping("/views/users/login")
    public String userLoginForm() {
        return "user/login";
    }

    // 회원가입 폼
    @GetMapping("/views/users/signup")
    public String userSignUpForm(Model model) {
        model.addAttribute("createUserRequest", new CreateUserRequest());

        return "user/signup";
    }

    // 마이페이지 폼
    @GetMapping("/views/users/mypage")
    public String userMyPage(Model model) {
        return "user/mypage";
    }

    @GetMapping("/views/users/edit")
    public String userEditFrom(Model model) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "redirect:/views/users/login";
        }
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String username = principal.getUsername();
        UserResponse userResponse = userService.findByUsername(username);
        model.addAttribute("updateUserRequest", UpdateUserRequest.toResponseDto(userResponse));

        return "user/edit";
    }

    // 유저별 인터뷰(연습모드) 이력 리스트
    @GetMapping("/views/users/interviews/practice")
    public String listInterviews(@RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        // 페이징 처리 기본값 설정
        int currentPage = page.orElse(1); // 기본값 1
        int pageSize = size.orElse(10); // 기본값 10

        // 페이지 사이즈가 1보다 작으면 기본값으로 설정
        if (pageSize < 1) {
            pageSize = 10; // 기본 페이지 사이즈
        }

        // 현재 인증된 사용자 ID 가져오기
        Long userId = userDetails.getUserId();

        // 페이지네이션된 인터뷰 목록 가져오기
        Page<PracticeInterviewHistorySummaryResponse> interviewSummaryList = userService.getPaginatedPracticeInterviews(
                PageRequest.of(currentPage - 1, pageSize), userId, Mode.PRACTICE);

        model.addAttribute("interviewSummaryList", interviewSummaryList);

        int totalPages = interviewSummaryList.getTotalPages();

        // 페이지 번호 리스트 생성
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/practice_interview_list";
    }

    // 유저 인터뷰(실전모드) 이력 리스트
    @GetMapping("/views/users/interviews/real")
    public String realInterviews(@RequestParam("page") Optional<Integer> page,
        @RequestParam("size") Optional<Integer> size,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model) {

        // 페이징 처리 기본값 설정
        int currentPage = page.orElse(1); // 기본값 1
        int pageSize = size.orElse(10); // 기본값 10

        // 페이지 사이즈가 1보다 작으면 기본값으로 설정
        if (pageSize < 1) {
            pageSize = 10; // 기본 페이지 사이즈
        }

        // 현재 인증된 사용자 ID 가져오기
        Long userId = userDetails.getUserId();

        // 페이지네이션된 인터뷰 목록 가져오기
        Page<PracticeInterviewHistorySummaryResponse> interviewSummaryList = userService.getPaginatedPracticeInterviews(
            PageRequest.of(currentPage - 1, pageSize), userId, Mode.REAL);

        model.addAttribute("interviewSummaryList", interviewSummaryList);

        int totalPages = interviewSummaryList.getTotalPages();

        // 페이지 번호 리스트 생성
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/real_interview_list";
    }

    @GetMapping("/views/users/interviews/real/{id}")
    public String realInterviewDetail(@PathVariable Long id, Model model) {

        MyPageInterviewWithDetailsDto interviewDetails = userService.getInterviewDetail(id);
        model.addAttribute("interviewDetails", interviewDetails);

        return "user/real_interview_detail";
    }

    // 회원가입 요청
    @PostMapping("/api/users/signup")
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

    // 회원정보 수정 요청
    @PutMapping("/api/users/edit")
    public String userEdit(@Valid UpdateUserRequest updateUserRequest,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없거나 인증되지 않은 사용자인 경우 로그인 페이지로 리디렉션
        if (userDetails == null) {
            return "redirect:/views/users/login";
        }

        // 사용자 정보 가져오기
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

    // 로그아웃
    @GetMapping("/api/users/logout")
    public String userLogout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy()
                .getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    // 회원 탈퇴 요청(RESTful API)
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

