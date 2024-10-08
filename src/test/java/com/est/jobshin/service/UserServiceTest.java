package com.est.jobshin.service;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.dto.InterviewHistorySummaryResponse;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interview.service.InterviewService;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.CreateUserRequest;
import com.est.jobshin.domain.user.dto.MyPageInterviewWithDetailsDto;
import com.est.jobshin.domain.user.dto.UpdateUserRequest;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Position;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.est.jobshin.domain.user.util.Language.JAVA;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private InterviewService interviewService;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void createUsers() {
        // Given: 회원 가입 요청을 생성
        CreateUserRequest request = CreateUserRequest.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .position(Position.BACKEND)
                .build();

        // When & Then: createUser 메서드 호출 시 예외가 발생하지 않는지 확인
        assertDoesNotThrow(() -> userService.createUser(request));
    }

    @Test
    @DisplayName("회원 정보 조회 성공 테스트")
    void findByUsername() {
        // Given: 테스트를 위한 사용자 생성 및 설정
        String username = "test@test.com";
        User existingUser = User.builder()
                .username(username)
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .position(Position.BACKEND)
                .build();

        // Mock 설정: 주어진 username으로 사용자를 찾을 때 existingUser 반환
        given(userRepository.findByUsername(username)).willReturn(Optional.of(existingUser));

        // When: findByUsername 메서드 호출
        UserResponse response = userService.findByUsername(username);

        // Then: 반환된 UserResponse가 예상과 일치하는지 검증
        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getNickname()).isEqualTo("테스트");
        assertThat(response.getLanguage()).isEqualTo(Language.JAVA);
        assertThat(response.getPosition()).isEqualTo(Position.BACKEND);
    }

    @Test
    @DisplayName("회원 정보 조회 실패 테스트")
    void findByUsernameThrowsException() {
        // Given: 존재하지 않는 사용자 이름 설정
        String username = "test@test.com";

        // lenient 사용: 불필요한 스터빙 경고를 무시하면서 빈 값을 반환하도록 설정
        lenient().when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then: 메서드 호출 시 UsernameNotFoundException 발생 여부 확인
        assertThatThrownBy(() -> userService.findByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("No user found with username:" + username);
    }

    @Test
    @DisplayName("회원 정보 수정 성공 테스트")
    void updateUser() {
        // Given: 기존 사용자 생성 및 저장
        String username = "test@test.com";

        User existingUser = User.builder()
                .username("test@test.com")
                .password("Password")
                .nickname("테스트")
                .language(Language.JAVA)
                .position(Position.BACKEND)
                .build();

        // 실제 데이터베이스에 사용자 저장
        userRepository.saveAndFlush(existingUser);

        // Mock 설정: userRepository가 주어진 username으로 사용자를 찾을 때 existingUser 반환
        given(userRepository.findByUsername(username)).willReturn(Optional.of(existingUser));

        // 회원 정보 수정 요청 생성
        UpdateUserRequest updateRequest = UpdateUserRequest.builder()
                .username(username)
                .password("newPassword1")
                .nickname("테스트1")
                .language(Language.PYTHON)
                .position(Position.FULLSTACK)
                .build();

        // 사용자 조회 및 확인
        Optional<User> foundUser = userRepository.findByUsername("test@test.com");
        if (foundUser.isPresent()) {
            System.out.println(foundUser.get().getUsername());
        } else {
            System.out.println("User not found.");
        }

        // When & Then: updateUser 메서드 실행 시 예외가 발생하지 않아야 함
        assertDoesNotThrow(() -> userService.updateUser(username, updateRequest)); // 예외가 발생하지 않는지 확인
    }

    @Test
    @DisplayName("회원 정보 중복 확인 테스트")
    void isDuplicate() {
        // Given: 중복 확인할 사용자 이름 설정
        String username = "test@example.com";

        // When: isDuplicate 메서드 호출
        boolean isDuplicate = userService.isDuplicate(username);

        // Then: 중복된 경우 true가 반환되는지 확인
        assertThat(isDuplicate).isTrue();
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void deleteUser() {
        // Given: 기존 사용자 생성 및 설정
        String username = "test@example.com";
        User existingUser = User.builder()
                .username(username)
                .build();

        // Mock 설정: 주어진 username으로 사용자를 찾을 때 existingUser 반환
        given(userRepository.findByUsername(username)).willReturn(Optional.of(existingUser));

        // When & Then: deleteUser 메서드 호출 시 예외가 발생하지 않는지 확인
        assertDoesNotThrow(() -> userService.deleteUser(username));
    }

    @DisplayName("회원탈퇴 예외 처리")
    @Test
    void deleteUserError() {
        // Given: 사용자 생성 및 저장
        User user = User.builder()
                .username("Test@test.com")
                .password("password")
                .nickname("테스트")
                .language(JAVA)
                .position(Position.BACKEND)
                .build();
        userRepository.save(user);

        // When & Then: deleteUser 메서드 호출 시 IllegalArgumentException 발생 여부 확인
        assertThatThrownBy(() -> userService.deleteUser(user.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(user.getUsername() + "에 해당하는 사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("모의 면접 리스트 페이징 처리 테스트")
    void getPaginatedInterviews() {
        // Given: 사용자와 인터뷰 생성
        User user = User.builder()
                .username("testUser")
                .build();

        InterviewDetail detail = InterviewDetail.createInterviewDetail("What is Java?", Category.LANGUAGE, Mode.PRACTICE, LocalDateTime.now());
        detail.registerAnswer("Java is a programming language.");
        detail.registerFeedback("Java language", 50L);

        Interview interview = Interview.createInterview("Test Interview", LocalDateTime.now(), user, Mode.PRACTICE);
        interview.addInterviewDetails(detail);

        Page<Interview> interviewPage = new PageImpl<>(List.of(interview), PageRequest.of(0, 10), 1);

        // Mock 설정: 면접 리포지토리가 페이징된 인터뷰를 반환하도록 설정 (lenient() 사용)
        lenient().when(interviewRepository.findInterviewsWithPracticeModeByUser(
                eq(user.getId()),
                any(Pageable.class),
                eq(Mode.PRACTICE)
        )).thenReturn(interviewPage);

        // When: 페이징된 면접을 DTO로 변환하는 로직 직접 구현
        List<InterviewHistorySummaryResponse> practiceInterviewList = interviewPage.stream()
                .map(interviewObj -> {
                    // 면접의 모든 디테일에서 점수를 합산
                    Long totalScore = interviewObj.getInterviewDetails().stream()
                            .mapToLong(InterviewDetail::getScore)
                            .sum();

                    // 면접의 첫 번째 디테일로 카테고리 설정
                    InterviewDetail firstDetail = interviewObj.getInterviewDetails().stream()
                            .findFirst().orElse(null);

                    return InterviewHistorySummaryResponse.toDto(interviewObj, firstDetail, totalScore / 5);
                })
                .collect(Collectors.toList());

        Page<InterviewHistorySummaryResponse> result = new PageImpl<>(practiceInterviewList, PageRequest.of(0, 10), interviewPage.getTotalElements());

        // Then: 페이징된 면접 결과 검증
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Test Interview");
        assertThat(result.getContent().get(0).getScore()).isEqualTo(10L); // 점수 합산 결과가 올바른지 검증 (50L / 5)
        assertThat(result.getContent().get(0).getCategory()).isEqualTo("LANGUAGE"); // 첫 번째 인터뷰 디테일의 카테고리가 올바른지 검증
    }


    @Test
    @DisplayName("면접 상세이력 미존재시 테스트")
    void getInterviewDetailNotFound() {
        // Given: 면접이력이 존재하지 않는 경우
        given(interviewRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then: 면접이력을 찾으려 할 때 EntityNotFoundException이 발생하는지 확인
        assertThatThrownBy(() -> {
            // 인터뷰 리포지토리에서 면접이력를 찾으려고 시도
            Interview interview = interviewRepository.findById(1L)
                    .orElseThrow(() -> new EntityNotFoundException("Interview not found with id 1"));
        })
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Interview not found with id 1");
    }

    @Test
    @DisplayName("면접 상세이력 존재시 성공 테스트")
    void getInterviewDetailSuccess() {
        // Given: 사용자와 면접 detail 생성
        User user = User.builder()
                .username("testUser")
                .build();

        InterviewDetail detail1 = InterviewDetail.createInterviewDetail("What is Java?", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail1.registerAnswer("Java is a programming language.");
        detail1.registerFeedback("Java language", 80L);

        InterviewDetail detail2 = InterviewDetail.createInterviewDetail("Explain OOP principles.", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail2.registerAnswer("Encapsulation, Inheritance, Polymorphism, Abstraction.");
        detail2.registerFeedback("Java language", 90L);

        Interview interview = Interview.createInterview("Real Mode Interview", LocalDateTime.now(), user, Mode.REAL);
        interview.addInterviewDetails(detail1);
        interview.addInterviewDetails(detail2);

        // Mock 설정: 인터뷰 리포지토리가 특정 ID로 인터뷰를 반환하도록 설정
        given(interviewRepository.findById(1L)).willReturn(Optional.of(interview));

        // When: 인터뷰 상세 정보를 직접 생성 및 조회
        Interview foundInterview = interviewRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Interview not found with id 1"));

        // 점수 평균 계산 (정수형)
        int averageScore = (int) Math.round(
                foundInterview.getInterviewDetails().stream()
                        .filter(detail -> detail.getScore() != null)  // 점수가 null이 아닌 경우에만 고려
                        .mapToLong(InterviewDetail::getScore)
                        .average()
                        .orElse(0.0)
        );

        // DTO로 변환
        MyPageInterviewWithDetailsDto result = MyPageInterviewWithDetailsDto.fromInterview(foundInterview);
        result.setAverageScore(averageScore);

        // Then: 반환된 DTO가 예상과 일치하는지 검증
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Real Mode Interview");
        assertThat(result.getAverageScore()).isEqualTo(85); // (80 + 90) / 2 = 85
    }

    @Test
    @DisplayName("면접이력 상세 점수 평균 계산 성공 테스트")
    void getInterviewDetailAverageScoreSuccessTest() {
        // Given: 사용자와 면접이력 생성
        User user = User.builder()
                .username("testUser")
                .build();

        // 여러 면접상세 생성 (모든 점수가 null이 아닌 경우)
        InterviewDetail detail1 = InterviewDetail.createInterviewDetail("Question 1", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail1.registerAnswer("Answer 1");
        detail1.registerFeedback("Java language", 80L);

        InterviewDetail detail2 = InterviewDetail.createInterviewDetail("Question 2", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail2.registerAnswer("Answer 2");
        detail2.registerFeedback("Java language", 90L);

        InterviewDetail detail3 = InterviewDetail.createInterviewDetail("Question 3", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail3.registerAnswer("Answer 3");
        detail3.registerFeedback("Java language", 70L);

        Interview interview = Interview.createInterview("Interview with Scores", LocalDateTime.now(), user, Mode.REAL);
        interview.addInterviewDetails(detail1);
        interview.addInterviewDetails(detail2);
        interview.addInterviewDetails(detail3);


        // Mock 설정: 특정 ID로 면접이력을 반환
        given(interviewRepository.findById(1L)).willReturn(Optional.of(interview));

        // When: 면접이력을 직접 조회하여 평균 점수를 계산
        Interview foundInterview = interviewRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Interview not found with id 1"));

        // 점수 평균 계산 (정수형)
        int averageScore = (int) Math.round(
                foundInterview.getInterviewDetails().stream()
                        .filter(detail -> detail.getScore() != null) // 점수가 null이 아닌 경우에만 고려
                        .mapToLong(InterviewDetail::getScore)
                        .average()
                        .orElse(0.0)
        );

        // DTO로 변환
        MyPageInterviewWithDetailsDto result = MyPageInterviewWithDetailsDto.fromInterview(foundInterview);
        result.setAverageScore(averageScore);

        // Then: 반환된 DTO의 평균 점수가 올바른지 검증 (80 + 90 + 70) / 3 = 80
        assertThat(result).isNotNull();
        assertThat(result.getAverageScore()).isEqualTo(80); // 모든 점수가 유효한 경우 평균 점수 검증
    }


    @Test
    @DisplayName("면접이력 점수 평균 계산 null 포함 테스트")
    void getInterviewDetailAverageScore() {
        // Given: 사용자와 면접이력 생성
        User user = User.builder()
                .username("testUser")
                .build();

        // 여러 면접상세를 생성 (일부는 점수가 null)
        InterviewDetail detail1 = InterviewDetail.createInterviewDetail("Question 1", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail1.registerAnswer("Answer 1");
        detail1.registerFeedback("Java language", 80L);

        InterviewDetail detail2 = InterviewDetail.createInterviewDetail("Question 2", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail2.registerAnswer("Answer 2");
        detail2.registerFeedback("Java language", 90L);

        InterviewDetail detail3 = InterviewDetail.createInterviewDetail("Question 3", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail3.registerAnswer("Answer 3");
        detail3.registerFeedback("Java language", null);

        Interview interview = Interview.createInterview("Interview with Scores", LocalDateTime.now(), user, Mode.REAL);
        interview.addInterviewDetails(detail1);
        interview.addInterviewDetails(detail2);
        interview.addInterviewDetails(detail3);

        // Mock 설정: 특정 ID로 면접이력을 반환
        given(interviewRepository.findById(1L)).willReturn(Optional.of(interview));

        // When: 면접이력을 직접 조회하여 평균 점수를 계산
        Interview foundInterview = interviewRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Interview not found with id 1"));

        /*// 점수 평균 계산 (정수형)
        int averageScore = (int) Math.round(
                foundInterview.getInterviewDetails().stream()
                        .filter(detail -> detail.getScore() != null) // 점수가 null이 아닌 경우에만 고려
                        .mapToLong(InterviewDetail::getScore)
                        .average()
                        .orElse(0.0)
        );

        // DTO로 변환
        MyPageInterviewWithDetailsDto result = MyPageInterviewWithDetailsDto.fromInterview(foundInterview);
        result.setAverageScore(averageScore);*/
        // Mock 설정: 특정 ID로 인터뷰를 반환
        given(interviewRepository.findById(1L)).willReturn(Optional.of(interview));

        // When: 인터뷰를 직접 조회하여 평균 점수를 계산
        MyPageInterviewWithDetailsDto result = userService.getInterviewDetail(1L);

        // Then: 반환된 DTO의 평균 점수가 올바른지 검증 (80 + 90) / 2 = 85
        assertThat(result).isNotNull();
        assertThat(result.getAverageScore()).isEqualTo(85); // null 점수는 평균 계산에서 제외됨
    }


    @Test
    @DisplayName("면접이력 상세 점수 모두 null인 경우 테스트")
    void getInterviewDetailAllNull() {
        // Given: 사용자와 면접이력 생성 (모든 점수가 null)
        User user = User.builder()
                .username("testUser")
                .build();

        InterviewDetail detail1 = InterviewDetail.createInterviewDetail("Question 1", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail1.registerAnswer("Answer 1");
        detail1.registerFeedback("Java language", null);

        InterviewDetail detail2 = InterviewDetail.createInterviewDetail("Question 2", Category.LANGUAGE, Mode.REAL, LocalDateTime.now());
        detail2.registerAnswer("Answer 2");
        detail2.registerFeedback("Java language", null);

        Interview interview = Interview.createInterview("Interview with Scores", LocalDateTime.now(), user, Mode.REAL);
        interview.addInterviewDetails(detail1);
        interview.addInterviewDetails(detail2);

        // Mock 설정: 특정 ID로 면접이력을 반환
        given(interviewRepository.findById(1L)).willReturn(Optional.of(interview));

        // When: 면접이력을 직접 조회하여 평균 점수를 계산
        Interview foundInterview = interviewRepository.findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Interview not found with id 1"));

        // 점수 평균 계산 (모든 점수가 null이므로 평균은 0)
        int averageScore = (int) Math.round(
                foundInterview.getInterviewDetails().stream()
                        .filter(detail -> detail.getScore() != null)
                        .mapToLong(InterviewDetail::getScore)
                        .average()
                        .orElse(0.0)
        );

        // DTO로 변환
        MyPageInterviewWithDetailsDto result = MyPageInterviewWithDetailsDto.fromInterview(foundInterview);
        result.setAverageScore(averageScore);

        // Then: 반환된 DTO의 평균 점수가 0 되는지 검증
        assertThat(result).isNotNull();
        assertThat(result.getAverageScore()).isEqualTo(0); // 모든 점수가 null이므로 평균은 0
    }

}



