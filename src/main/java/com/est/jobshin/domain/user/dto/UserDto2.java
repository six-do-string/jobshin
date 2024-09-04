package com.est.jobshin.domain.user.dto;

import com.est.jobshin.domain.user.util.Level;
import lombok.Builder;

@Builder
public class UserDto2 {
    private Long id;
    private Level level;
}
