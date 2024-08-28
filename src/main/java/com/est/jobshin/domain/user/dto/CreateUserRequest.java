package com.est.jobshin.domain.user.dto;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    // RFC 5322 이메일 정규식
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "아이디는 이메일 형태로 입력해주세요.")
    @NotBlank(message = "아이디는 빈칸을 입력할 수 없습니다.")
    private String username;

    // 비밀번호: 영문, 숫자, 특수문자를 포함하여 8~15자 작성
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 최소 8글자 이상이어야 합니다.")
    @NotBlank(message = "비밀번호는 빈칸을 입력할 수 없습니다.")
    private String password;

    @NotBlank(message = "이름은 빈칸을 입력할 수 없습니다.")
    private String nickname;

    private Language language;
    private Position position;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .language(language)
                .position(position)
                .build();
    }
}