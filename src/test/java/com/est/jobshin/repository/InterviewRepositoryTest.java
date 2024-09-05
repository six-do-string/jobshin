package com.est.jobshin.repository;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
import com.est.jobshin.domain.levelfeedback.domain.LevelFeedback;
import com.est.jobshin.domain.levelfeedback.repository.LevelFeedbackRepository;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 데이터베이스 사용 설정

class InterviewRepositoryTest {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InterviewDetailRepository interviewDetailRepository;

    @Autowired
    private LevelFeedbackRepository feedbackRepository;  // feedbackRepository 주입 추가

    @Test
    @DisplayName("사용자의 Practice 모드 인터뷰 조회 테스트")
    void InterviewDetailRepositoryPracticeMode() {
        // Given: User 객체를 생성하고 저장
        User user = User.builder()
                .username("testUser")
                .build();
        user = userRepository.save(user);

        // Given: Interview 객체 생성 및 저장
        Interview interview = Interview.builder()
                .title("Test Interview")
                .createdAt(LocalDateTime.now())
                .mode(Mode.PRACTICE)
                .user(user)
                .build();
        interviewRepository.save(interview);

        // When: 인터뷰 조회
        Page<Interview> interviews = interviewRepository.findInterviewsWithPracticeModeByUser(user.getId(), PageRequest.of(0, 10), Mode.PRACTICE);

        // Then: 인터뷰 내용 검증
        assertThat(interviews.getContent()).hasSize(1);
        assertThat(interviews.getContent().get(0).getTitle()).isEqualTo("Test Interview");
        assertThat(interviews.getContent().get(0).getMode()).isEqualTo(Mode.PRACTICE);
    }

    @Test
    @DisplayName("사용자의 REAL 모드 인터뷰 조회 테스트")
    void IInterviewDetailRepositoryRealMode() {
        // Given: User 객체를 생성하고 저장
        User user = User.builder()
                .username("testUser1")
                .build();
        user = userRepository.save(user);

        // Given: Interview 객체 생성 및 저장
        Interview interview = Interview.builder()
                .title("Test Interview1")
                .createdAt(LocalDateTime.now())
                .mode(Mode.REAL)
                .user(user)
                .build();
        interviewRepository.save(interview);

        // When: 면접이력 조회
        Page<Interview> interviews = interviewRepository.findInterviewsWithPracticeModeByUser(user.getId(), PageRequest.of(0, 10), Mode.REAL);

        // Then: 면접 내용 검증
        assertThat(interviews.getContent()).hasSize(1);
        assertThat(interviews.getContent().get(0).getTitle()).isEqualTo("Test Interview1");
        assertThat(interviews.getContent().get(0).getMode()).isEqualTo(Mode.REAL);
    }


    @Test
    @DisplayName("Interview ID로 InterviewDetail 조회 테스트")
    @Transactional
    void findByInterviewIdTest() {
        // given: 사용자와 면접 데이터 생성 및 저장
        User user = User.builder()
                .username("testUser")
                .password("password")
                .nickname("Tester")
                .language(Language.JAVA)
                .level(Level.LV2)
                .position(Position.BACKEND)
                .build();

        // 사용자 저장 및 검증
        User savedUser = userRepository.saveAndFlush(user);
        assertThat(savedUser.getId()).isNotNull(); // 저장된 사용자의 ID가 존재하는지 확인
        User foundUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));
        assertThat(foundUser).isNotNull(); // 저장된 사용자가 조회되는지 확인

        // 면접 생성 및 저장
        Interview interview = Interview.builder()
                .title("Test Interview")
                .createdAt(LocalDateTime.now())
                .mode(Mode.PRACTICE)
                .user(savedUser)
                .build();

        // 면접 저장 및 검증
        Interview savedInterview = interviewRepository.save(interview);
        assertThat(savedInterview.getId()).isNotNull(); // 저장된 면접의 ID가 존재하는지 확인

        // 면접 상세 내용 생성 및 저장
        InterviewDetail interviewDetail = InterviewDetail.builder()
                .question("What is Java?")
                .answer("Java is a programming language.")
                .category(Category.LANGUAGE)
                .exampleAnswer("Java language")
                .mode(Mode.PRACTICE)
                .score(50L)
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();

        // 상세 내용 저장 및 데이터베이스에 반영
        InterviewDetail savedDetail = interviewDetailRepository.save(interviewDetail);
        interviewRepository.saveAndFlush(interview); // 면접 객체를 즉시 반영

        // when: 특정 면접이력의 상세 내용을 조회
        List<InterviewDetail> details = interviewDetailRepository.findByInterviewId(savedInterview.getId());

        // then: 면접상세 검증
        assertThat(details).isNotEmpty(); // 면접 세부사항이 존재하는지 확인
        assertThat(details).hasSize(1); // 상세 내용의 개수가 올바른지 확인
        assertThat(details.get(0).getQuestion()).isEqualTo("What is Java?"); // 저장된 질문과 일치하는지 확인
        assertThat(details.get(0).getAnswer()).isEqualTo("Java is a programming language."); // 저장된 답변과 일치하는지 확인
    }

    @DisplayName("면접이력에 상세 내용이 확인하는 테스트")
    @Test
    void interviewDetailCheck() {
        // given: 사용자 생성 및 저장
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

        // 면접이력 생성 및 저장
        Interview interview = Interview.builder()
                .title("Java Developer Interview")
                .createdAt(LocalDateTime.now())
                .user(savedUser)
                .build();

        // 면접이력 저장
        Interview savedInterview = interviewRepository.save(interview);

        // 면접이력 상세 내용 생성 및 저장 - 각 객체가 고유하게 저장되어야 함
        InterviewDetail detail1 = InterviewDetail.builder()
                .question("What is Java?")
                .answer("Java is a programming language.")
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();
        InterviewDetail savedDetail1 = interviewDetailRepository.saveAndFlush(detail1); // 첫 번째 면접이력 상세 내용 저장
        assertThat(savedDetail1.getId()).isNotNull(); // 저장된 ID가 존재하는지 확인

        InterviewDetail detail2 = InterviewDetail.builder()
                .question("Explain OOP concepts.")
                .answer("OOP stands for Object-Oriented Programming.")
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();
        InterviewDetail savedDetail2 = interviewDetailRepository.saveAndFlush(detail2); // 두 번째 면접이력 상세 내용 저장
        assertThat(savedDetail2.getId()).isNotNull();

        InterviewDetail detail3 = InterviewDetail.builder()
                .question("What is Polymorphism?")
                .answer("Polymorphism allows objects to be treated as instances of their parent class.")
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();
        InterviewDetail savedDetail3 = interviewDetailRepository.saveAndFlush(detail3); // 세 번째 면접이력 상세 내용 저장
        assertThat(savedDetail3.getId()).isNotNull();

        InterviewDetail detail4 = InterviewDetail.builder()
                .question("Explain Encapsulation.")
                .answer("Encapsulation is the concept of wrapping data and methods together.")
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();
        InterviewDetail savedDetail4 = interviewDetailRepository.saveAndFlush(detail4); // 네 번째 면접이력 상세 내용 저장
        assertThat(savedDetail4.getId()).isNotNull();

        InterviewDetail detail5 = InterviewDetail.builder()
                .question("Describe Inheritance.")
                .answer("Inheritance allows a class to inherit fields and methods from another class.")
                .createdAt(LocalDateTime.now())
                .interview(savedInterview)
                .build();
        InterviewDetail savedDetail5 = interviewDetailRepository.saveAndFlush(detail5); // 다섯 번째 면접이력 상세 내용 저장
        assertThat(savedDetail5.getId()).isNotNull();

        // when: 면접이력와 연결된 상세 내용을 조회
        List<InterviewDetail> details = interviewDetailRepository.findByInterviewId(savedInterview.getId());

        // then: 면접이력에 상세 내용이 있는지 검증
        assertThat(details).hasSize(5); // 상세 내용이 5개인지 확인
    }

    @Test
    @DisplayName("사용자 ID로 피드백 조회 테스트")
    void findByUserIdTest() {
        // Given: LevelFeedback 객체 생성 및 저장
        LevelFeedback feedback1 = new LevelFeedback(null, 1L, "Great feedback", 5L);
        LevelFeedback feedback2 = new LevelFeedback(null, 1L, "Needs improvement", 3L);
        feedbackRepository.save(feedback1);
        feedbackRepository.save(feedback2);

        // When: 사용자 ID로 피드백 조회
        List<LevelFeedback> feedbacks = feedbackRepository.findByUserId(1L);

        // Then: 레벨 검증
        assertThat(level).isEqualTo(Level.LV2); // 유저 레벨 검증
    }
}
