package com.est.jobshin.repository;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interview.repository.InterviewRepository;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.repository.InterviewDetailRepository;
import com.est.jobshin.domain.interviewDetail.util.Category;
import com.est.jobshin.domain.interviewDetail.util.Mode;
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


    @Test
    @DisplayName("사용자의 Practice 모드 인터뷰 조회 테스트")
    void InterviewDetailRepositoryPracticeMode() {
        // Given: User 객체를 생성하고 저장
        User user = User.builder()
                .username("testUser")
                .build();
        user = userRepository.save(user);

        // Given: Interview 객체 생성 및 저장
        Interview interview = Interview.createInterview("Test Interview", LocalDateTime.now(), user, Mode.PRACTICE);

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

        // Given: Interview 객체 생성 및 저장 (제목을 "Test Interview1"로 변경)
        Interview interview = Interview.createInterview("Test Interview1", LocalDateTime.now(), user, Mode.REAL);
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
        Interview interview = Interview.createInterview("Test Interview", LocalDateTime.now(), savedUser, Mode.PRACTICE);

        // 면접 저장 및 검증
        Interview savedInterview = interviewRepository.save(interview);
        assertThat(savedInterview.getId()).isNotNull(); // 저장된 면접의 ID가 존재하는지 확인

        // 면접 상세 내용 생성 및 저장
        InterviewDetail interviewDetail = InterviewDetail.createInterviewDetail("What is Java?", Category.LANGUAGE, Mode.PRACTICE, LocalDateTime.now());
        interviewDetail.setAnswer("Java is a programming language.");
        interviewDetail.setExampleAnswer("Java language");
        interviewDetail.setScore(50L);
        interviewDetail.setInterview(savedInterview);

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
        Interview interview = Interview.createInterview("Java Develop Interview", LocalDateTime.now(), savedUser, null);

        // 면접이력 저장
        Interview savedInterview = interviewRepository.save(interview);

        // 면접이력 상세 내용 생성 및 저장 - 각 객체가 고유하게 저장되어야 함
        InterviewDetail detail1 = InterviewDetail.createInterviewDetail("What is Java?", null, null, LocalDateTime.now());
        detail1.setAnswer("Java is a programming language.");
        detail1.setInterview(savedInterview);

        InterviewDetail savedDetail1 = interviewDetailRepository.saveAndFlush(detail1); // 첫 번째 면접이력 상세 내용 저장
        assertThat(savedDetail1.getId()).isNotNull(); // 저장된 ID가 존재하는지 확인


        InterviewDetail detail2 = InterviewDetail.createInterviewDetail("Explain OOP concepts.", null, null, LocalDateTime.now());
        detail2.setAnswer("OOP stands for Object-Oriented Programming.");
        detail2.setInterview(savedInterview);

        InterviewDetail savedDetail2 = interviewDetailRepository.saveAndFlush(detail2); // 두 번째 면접이력 상세 내용 저장
        assertThat(savedDetail2.getId()).isNotNull();


        InterviewDetail detail3 = InterviewDetail.createInterviewDetail("What is Polymorphism?", null, null, LocalDateTime.now());
        detail3.setAnswer("Polymorphism allows objects to be treated as instances of their parent class.");
        detail3.setInterview(savedInterview);

        InterviewDetail savedDetail3 = interviewDetailRepository.saveAndFlush(detail3); // 세 번째 면접이력 상세 내용 저장
        assertThat(savedDetail3.getId()).isNotNull();


        InterviewDetail detail4 = InterviewDetail.createInterviewDetail("Explain Encapsulation.", null, null, LocalDateTime.now());
        detail4.setAnswer("Encapsulation is the concept of wrapping data and methods together.");
        detail4.setInterview(savedInterview);

        InterviewDetail savedDetail4 = interviewDetailRepository.saveAndFlush(detail4); // 네 번째 면접이력 상세 내용 저장
        assertThat(savedDetail4.getId()).isNotNull();


        InterviewDetail detail5 = InterviewDetail.createInterviewDetail("Describe Inheritance.", null, null, LocalDateTime.now());
        detail5.setAnswer("Inheritance allows a class to inherit fields and methods from another class.");
        detail5.setInterview(savedInterview);

        InterviewDetail savedDetail5 = interviewDetailRepository.saveAndFlush(detail5); // 다섯 번째 면접이력 상세 내용 저장
        assertThat(savedDetail5.getId()).isNotNull();

        // when: 면접이력와 연결된 상세 내용을 조회
        List<InterviewDetail> details = interviewDetailRepository.findByInterviewId(savedInterview.getId());

        // then: 면접이력에 상세 내용이 있는지 검증
        assertThat(details).hasSize(5); // 상세 내용이 5개인지 확인
    }

    @Test
    @DisplayName("사용자명을 이용해 레벨 조회 테스트")
    void findLevelByUsername() {
        // Given: 사용자 저장
        User user = User.builder()
                .username("levelTestUser")
                .password("password")
                .nickname("Tester")
                .language(Language.JAVA)
                .level(Level.LV2) // 설정된 레벨
                .position(Position.BACKEND)
                .build();
        userRepository.save(user);

        // When: 사용자명으로 사용자 조회 후 레벨 확인
        Level level = userRepository.findByUsername("levelTestUser")
                .map(User::getLevel)
                .orElseThrow(() -> new AssertionError("User not found"));

        // Then: 레벨 검증
        assertThat(level).isEqualTo(Level.LV2); // 유저 레벨 검증
    }
}
