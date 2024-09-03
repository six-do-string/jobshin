package com.est.jobshin.repository;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.est.jobshin.domain.interviewDetail.util.Mode.PRACTICE;
import static com.est.jobshin.domain.user.util.Language.JAVA;
import static com.est.jobshin.domain.user.util.Language.PYTHON;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 사용 설정
public class UserRepositoryTest {

    // 의존성 주입: UserRepository, InterviewRepository, InterviewDetailRepository
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewDetailRepository interviewDetailRepository;

    // 로깅 설정
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryTest.class);

    // 각 테스트가 실행된 후 데이터베이스를 정리하는 메서드
    @AfterEach
    void tearDown() {
        // 테스트 후 인터뷰와 사용자 데이터를 삭제하여 깨끗한 상태로 유지
        interviewRepository.deleteAll();
        userRepository.deleteAll();
    }
    // -- 회원 영역 테스트
    // --- 회원가입 레포지토리 테스트 ---

    // 유저 생성시 입력한 데이터가 맞게 생성되었는지. 레벨과 생성시간 변경시간이 생성되었는지 확인
    @DisplayName("유저 생성 데이터 테스트")
    @Test
    void createUser() {
        //Given
        User user = User.builder()
                .username("Test@test.com")
                .password("password")
                .nickname("테스트")
                .language(JAVA)
                .position(Position.BACKEND)
                .build();

        userRepository.save(user);

        //When
        Optional<User> foundUser = userRepository.findByUsername("Test@test.com");

        //Then
        // 입력한 값이 맞는가 확인
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(foundUser.get().getPassword()).isEqualTo(user.getPassword());
        assertThat(foundUser.get().getNickname()).isEqualTo(user.getNickname());
        assertThat(foundUser.get().getLanguage()).isEqualTo(user.getLanguage());
        assertThat(foundUser.get().getPosition()).isEqualTo(user.getPosition());

        // 자동으로 입력되는 값이 들어갔나 확인
        assertThat(foundUser.get().getLevel()).isNotNull();
        assertThat(foundUser.get().getCreatedAt()).isNotNull();
        assertThat(foundUser.get().getUpdatedAt()).isNotNull();
    }

    // --- 회원정보 수정 레포지토리 테스트 ---
    @DisplayName("정보수정 update시간 변경 테스트")
    @Test
    void modifyUser() {
        // Given: 사용자 객체 생성 및 저장
        User user = User.builder()
                .username("Test@test.com")
                .password("password")
                .nickname("테스트")
                .language(JAVA)
                .position(Position.BACKEND)
                .build();

        // 사용자 저장
        userRepository.saveAndFlush(user); // saveAndFlush 사용하여 즉시 반영

        // When: 사용자 업데이트
        LocalDateTime initialUpdateTime = user.getUpdatedAt(); // 처음 업데이트 시간을 기록
        Optional<User> foundUser = userRepository.findByUsername("Test@test.com");
        foundUser.get().updateUser("password1", "테스트", PYTHON, Position.BACKEND);

        // 업데이트 후 데이터베이스에 반영
        userRepository.saveAndFlush(foundUser.get()); // 변경된 내용을 데이터베이스에 즉시 반영

        // Then: 업데이트 시간이 변경되었는지 검증
        assertThat(foundUser.get().getUpdatedAt()).isAfter(initialUpdateTime); // 시간이 업데이트된 이후인지 확인
    }

    @DisplayName("회원정보 수정 기능 테스트")
    @Test
    void updateUser() {
        // given: 초기 사용자 데이터 생성 및 저장
        User user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(JAVA)
                .position(Position.BACKEND)
                .build();

        User savedUser = userRepository.save(user);

        //when : 사용자 정보 수정
        savedUser.updateUser("Password1", "테스트1", PYTHON, Position.FULLSTACK);
        userRepository.save(savedUser);

        //then : 수정된 사용자 정보가 저장되었는지 확인
        Optional<User> updateUser = userRepository.findByUsername("test@test.com");
        assertThat(updateUser).isPresent();
        assertThat(updateUser.get().getPassword()).isEqualTo("Password1");
        assertThat(updateUser.get().getNickname()).isEqualTo("테스트1");
        assertThat(updateUser.get().getLanguage()).isEqualTo(Language.PYTHON);
        assertThat(updateUser.get().getPosition()).isEqualTo(Position.FULLSTACK);

    }

    // --- 회원정보 탈퇴 레포지토리 테스트 ---
    @DisplayName("탈퇴 회원정보 삭제 테스트")
    @Test
    void deleteUser() {
        // given: 초기 사용자 데이터 생성 및 저장
        User user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(JAVA)
                .position(Position.BACKEND)
                .build();

        User savedUser = userRepository.save(user);

        //when : 사용자 삭제
        userRepository.delete(savedUser);

        //then : 삭제된 사용자 정보가 존재하지 않는지 확인
        Optional<User> deletedUser = userRepository.findByUsername("test@test.com");
        assertThat(deletedUser).isNotPresent();
    }
    // --

    // -- 유저 인터뷰 이력 테스트
    @DisplayName("사용자가 면접 이력이 미존재시 테스트")
    @Test
    void NoInterviewHistoryTest() {
        // given: 면접 없이 사용자만 생성하여 저장
        User user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        User savedUser = userRepository.save(user);

        // when: 모든 면접을 가져온 후 특정 사용자의 면접만 필터링
        List<Interview> interviews = interviewRepository.findAll().stream()
                .filter(interviewItem -> interviewItem.getUser().getUsername().equals("test@test.com"))
                .collect(Collectors.toList());

        // then: 면접 이력이 없는지 검증
        assertThat(interviews).isEmpty(); // isEmpty()로 수정하여 면접 이력이 없는지 확인
    }

    @DisplayName("사용자가 면접 이력이 존재시 테스트")
    @Test
    @Transactional
    void interviewHistory() {
        // given: 사용자와 면접 데이터 생성 및 저장
        User user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // 사용자 저장 및 검증
        User savedUser = userRepository.saveAndFlush(user);
        assertThat(savedUser.getId()).isNotNull(); // 저장된 사용자의 ID가 존재하는지 확인
        User foundUser = userRepository.findById(savedUser.getId()).orElseThrow(() -> new AssertionError("User not found"));
        assertThat(foundUser).isNotNull(); // 저장된 사용자가 조회되는지 확인

        // 코드에러 확인
//        assertThat(foundUser.getUsername()).isEqualTo("test@test.com");
//        logger.info("Saved User ID: " + savedUser.getId());
//        logger.info("Found User ID: " + foundUser.getId());

        // 면접 생성 및 저장
        Interview interview = Interview.builder()
                .title("java test")
                .createAt(LocalDateTime.now())
                .mode(PRACTICE)
                .user(savedUser)
                .build();

        // 면접 저장 및 검증
        Interview savedInterview = interviewRepository.save(interview);
        assertThat(interview.getId()).isNotNull(); // 저장된 면접의 ID가 존재하는지 확인

//    // 코드에러 확인
//        logger.info("Saved Interview ID: " + savedInterview.getId());
//        logger.info("Saved Interview User ID: " + savedInterview.getUser().getId());

        // 면접 상세 내용 생성 및 저장
        InterviewDetail interviewDetail = InterviewDetail.builder()
                .question("What is Java?")
                .answer("Java is a programming language.")
                .category(Category.LANGUAGE)
                .exampleAnswer("Java language")
                .mode(PRACTICE)
                .score(50L)
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();

        // 상세 내용 저장 및 데이터베이스에 반영
        InterviewDetail savedDetail1 = interviewDetailRepository.save(interviewDetail);
        interviewRepository.saveAndFlush(interview); // 면접 객체를 즉시 반영

        // when: 특정 사용자의 면접만 필터링
        List<Interview> interviews = interviewRepository.findAll().stream()
                .filter(interviewItem -> interviewItem.getUser().getUsername().equals("test@test.com"))
                .collect(Collectors.toList());

        // then: 면접 이력이 있는지 검증
        assertThat(interviews).isNotEmpty(); // 면접 이력이 있는지 확인
    }

    @DisplayName("유저의 인터뷰 건수와 내용이 일치여부 확인 테스트")
    @Test
    void interviewCountAndContent() {
        // given: 사용자와 인터뷰 데이터 생성 및 저장
        User user = User.builder()
                .username("test@test.com")
                .password("password")
                .nickname("테스트")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // 사용자 저장
        User savedUser = userRepository.save(user);

        // 인터뷰 데이터 생성 및 저장
        Interview interview = Interview.builder()
                .title("java test")
                .createAt(LocalDateTime.now())
                .user(savedUser) // 저장된 User 객체를 설정
                .build();

        // 인터뷰 저장
        Interview savedInterview = interviewRepository.save(interview);

        // when: 특정 사용자의 인터뷰를 조회
        List<Interview> interviews = interviewRepository.findAll().stream()
                .filter(interviewItem -> interviewItem.getUser().getUsername().equals("test@test.com"))
                .collect(Collectors.toList());

        // then: 인터뷰 건수와 내용이 맞는지 검증
        assertThat(interviews).hasSize(1); // 인터뷰 건수가 1건인지 확인
        assertThat(interviews.get(0).getTitle()).isEqualTo(savedInterview.getTitle()); // 인터뷰 제목이 저장된 내용과 일치하는지 확인
        assertThat(interviews.get(0).getCreateAt()).isEqualToIgnoringSeconds(savedInterview.getCreateAt()); // 인터뷰 생성 시간이 일치하는지 확인
        assertThat(interviews.get(0).getUser().getUsername()).isEqualTo(savedUser.getUsername()); // 인터뷰가 올바른 사용자와 연결되었는지 확인
    }


}


