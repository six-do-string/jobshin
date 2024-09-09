package com.est.jobshin.domain.user.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewHistorySummaryResponse;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.*;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.util.Level;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    // 마이 페이지
    // 모의 면접 리스트 페이징 처리
    @Transactional(readOnly = true)
    public Page<InterviewHistorySummaryResponse> getPaginatedInterviews(Pageable pageable,
            Long userId, Mode mode) {

        // 인터뷰 목록을 페이지네이션으로 가져옴
        List<Interview> interviewsPage = interviewRepository.findInterviewsWithPracticeModeByUser(
                userId, mode);

        // 인터뷰 목록을 DTO로 변환
        List<InterviewHistorySummaryResponse> practiceInterviewList = interviewsPage.stream()
                .map(interview -> {
                    // 인터뷰의 모든 디테일에서 점수를 합산
                    Long totalScore = interview.getInterviewDetails().stream()
                            .mapToLong(InterviewDetail::getScore)
                            .sum();

                    // 인터뷰의 첫 번째 디테일로 카테고리 설정
                    InterviewDetail firstDetail = interview.getInterviewDetails().stream()
                            .findFirst().orElse(null);

                    return InterviewHistorySummaryResponse.toDto(interview, firstDetail,
                            totalScore / 5);

                })
                .collect(Collectors.toList());

        // 페이지네이션 처리 (practiceInterviewList 기준)
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), practiceInterviewList.size());

        // 페이지 내 범위에 해당하는 리스트 추출
        List<InterviewHistorySummaryResponse> paginatedList = practiceInterviewList.subList(start, end);

        // DTO 리스트를 페이지네이션된 결과로 반환
        return new PageImpl<>(paginatedList, pageable, practiceInterviewList.size());

    }


    // 모의면접(실전모드) 상세보기
    @Transactional(readOnly = true)
    public MyPageInterviewWithDetailsDto getInterviewDetail(Long id) {

        Interview interview = interviewRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Interview not found with id " + id));

        // 점수 평균 계산 (정수형)
        int averageScore = (int) Math.round(
                interview.getInterviewDetails().stream()
                        .filter(detail -> detail.getScore() != null)  // 점수가 null이 아닌 경우에만 고려
                        .mapToLong(InterviewDetail::getScore)
                        .average()
                        .orElse(0.0)
        );

        // DTO로 변환
        MyPageInterviewWithDetailsDto myPageInterviewWithDetailsDto = MyPageInterviewWithDetailsDto.fromInterview(
                interview);
        myPageInterviewWithDetailsDto.setAverageScore(averageScore);

        return myPageInterviewWithDetailsDto;
    }

    // 유저 레벨 업데이트
    public void updateUserLevel(String username, Double score) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("No user found with username:" + username));
        Level newLevel = score < 40 ? Level.LV1 : (score > 60 ? Level.LV3 : Level.LV2);
        user.updateUserLevel(newLevel);
        userRepository.save(user);
    }

}
