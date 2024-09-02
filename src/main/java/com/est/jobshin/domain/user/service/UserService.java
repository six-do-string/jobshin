package com.est.jobshin.domain.user.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.PracticeInterviewHistorySummaryResponse;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;

import com.est.jobshin.global.security.model.CustomUserDetails;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InterviewRepository interviewRepository;

    // 회원 가입 메서드
    @Transactional
    public void createUser(CreateUserRequest createUserRequest) {
        userRepository.save(createUserRequest.toEntity());
    }

    // username 으로 찾는 메서드
    @Transactional(readOnly = true)
    public UserResponse findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with username:" + username));

        return UserResponse.toDto(user);
    }

    // 회원 정보 수정 메서드
    @Transactional
    public void updateUser(String username, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No user found with username:" + username));
        user.updateUser(updateUserRequest.getPassword(), updateUserRequest.getNickname(),
                updateUserRequest.getLanguage(), updateUserRequest.getPosition());
        userRepository.save(user);
    }

    // username 필드 중복 확인 메서드
    @Transactional(readOnly = true)
    public boolean isDuplicate(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        return user.isEmpty();
    }

    // 회원 탈퇴 메서드
    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new IllegalArgumentException(username + "에 해당하는 사용자를 찾을 수 없습니다."));
        userRepository.delete(user);
    }

    // 마이페이지
    // 모의면접(연습모드) 리스트 페이지네이션
    @Transactional(readOnly = true)
    public Page<PracticeInterviewHistorySummaryResponse> getPaginatedPracticeInterviews(
            Pageable pageable, Long userId, Mode mode) {

        // 데이터베이스에서 페이지네이션된 인터뷰 목록을 가져옴
        Page<Interview> interviewsPage = interviewRepository.findInterviewsWithPracticeModeByUser(
                userId, pageable, mode);

        // 인터뷰 목록을 DTO로 변환
        List<PracticeInterviewHistorySummaryResponse> practiceInterviewList = interviewsPage.stream()
                .map(PracticeInterviewHistorySummaryResponse::toDto)
                .collect(Collectors.toList());

        // DTO 리스트를 페이지네이션된 결과로 반환
        return new PageImpl<>(practiceInterviewList, pageable, interviewsPage.getTotalElements());
    }

}
