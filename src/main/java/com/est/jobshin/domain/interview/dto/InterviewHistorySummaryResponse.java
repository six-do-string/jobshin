package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
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
public class InterviewHistorySummaryResponse {

    private Long id;        // Interview의 id
    private String title;
    private String nickname;
    private LocalDateTime createdAt; // Interview의 createAt
    private Long score;              // InterviewDetail의 score
    private String category;         // InterviewDetail의 category

    public static InterviewHistorySummaryResponse toDto(Interview interview, InterviewDetail interviewDetail, Long totalScore) {
        return InterviewHistorySummaryResponse.builder()
                .id(interview.getId())
                .title(interview.getTitle())
                .nickname(interview.getUser().getNickname())
                .createdAt(interview.getCreatedAt())
                .score(totalScore)
                .category(interviewDetail.getCategory().toString())
                .build();
    }
}
