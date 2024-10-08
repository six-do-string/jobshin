package com.est.jobshin.domain.user.dto;

import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.util.Language;
import com.est.jobshin.domain.user.util.Level;
import com.est.jobshin.domain.user.util.Position;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link com.est.jobshin.domain.user.domain.User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long id;
    private String username;
    private String password;
    private String nickname;


    private Language language;
    private Level level;
    private Position position;

    public static UserResponse toDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .language(user.getLanguage())
                .level(user.getLevel())
                .position(user.getPosition())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}