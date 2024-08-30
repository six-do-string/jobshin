package com.est.jobshin.repository;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.util.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.est.jobshin.domain.user.util.Language.JAVA;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
	}

	@DisplayName("유저 생성 테스트")
	@Test
	void createUser(){
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

	@DisplayName("정보수정")
	@Test
	void modifyUser(){
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
		String time = user.getUpdatedAt().toString();
		Optional<User> foundUser = userRepository.findByUsername("Test@test.com");
		foundUser.get().updateUser("password1","테스트",JAVA,Position.BACKEND);

	    //Then
		assertThat(foundUser.get().getUpdatedAt().toString()).isNotEqualTo(time);

	}





}
