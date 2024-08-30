package com.est.jobshin.service;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.repository.UserRepository;
import com.est.jobshin.domain.user.service.UserService;
import com.est.jobshin.domain.user.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static com.est.jobshin.domain.user.util.Language.JAVA;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@DisplayName("회원탈퇴 예외 처리")
	@Test
	void deleteUserError(){
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

	    //Then
		assertThatThrownBy(() -> userService.deleteUser(user.getUsername()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(user.getUsername() + "에 해당하는 사용자를 찾을 수 없습니다.");
	}

}
