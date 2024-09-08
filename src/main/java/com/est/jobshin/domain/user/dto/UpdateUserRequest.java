package com.est.jobshin.domain.user.dto;

import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private String username;

    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하여야 합니다.")
    @Pattern(regexp = "^(?:(?=.*[a-zA-Z])(?=.*[0-9])|(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])|(?=.*[0-9])(?=.*[!@#$%^*+=-])).*$",
            message = "비밀번호는 영문, 숫자, 특수문자 중 2가지 종류를 포함해야 합니다.")
    @NotBlank(message = "비밀번호는 빈칸을 입력할 수 없습니다.")
    private String password;
    @NotBlank(message = "이름은 빈칸을 입력할 수 없습니다.")
    private String nickname;
    private Language language;
    private Position position;

    public static UpdateUserRequest toResponseDto(UserResponse userResponse) {
        return UpdateUserRequest.builder()
                .username(userResponse.getUsername())
                .password(userResponse.getPassword())
                .nickname(userResponse.getNickname())
                .language(userResponse.getLanguage())
                .position(userResponse.getPosition())
                .build();
    }
}
