package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;

import com.est.jobshin.domain.user.util.Level;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 게시글을 요약한 상태로 수 있는 형태로 응답하는 DTO<br> DTo for
 * {@link Interview}
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewLevel {
    private Level level;

    public static InterviewLevel toDto(Interview interview, Long totalScore) {
        return InterviewLevel.builder()
                .level(interview.getUser().getLevel())
                .build();
    }
}
