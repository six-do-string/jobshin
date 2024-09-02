package com.est.jobshin.domain.interviewDetail.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterviewDetailsResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String title;
    private List<InterviewDetailDto2> details;

    public static InterviewDetailsResponse toDto(Interview interview, List<InterviewDetail> details) {
        return InterviewDetailsResponse.builder()
                .id(interview.getId())
                .createdAt(interview.getCreateAt())
                .title(interview.getTitle())
                .details(details.stream()
                        .map(detail -> InterviewDetailDto2.from(detail))
                        .collect(Collectors.toList()))
                .build();
    }
}
