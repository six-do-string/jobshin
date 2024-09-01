package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import java.time.LocalDateTime;
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
public class PracticeInterviewHistorySummaryResponse {

    private Long id; // 인터뷰 id
    private String nickname; // 인터뷰한 사용자
    private String title; // 인터뷰 제목
    private LocalDateTime createdAt; // 인터뷰 생성 일시

    public static PracticeInterviewHistorySummaryResponse toDto(Interview interview) {
        return PracticeInterviewHistorySummaryResponse.builder()
                .id(interview.getId())
                .title(interview.getTitle())
                .nickname(interview.getUser().getNickname())
                .createdAt(interview.getCreateAt())
                .build();
    }
}
