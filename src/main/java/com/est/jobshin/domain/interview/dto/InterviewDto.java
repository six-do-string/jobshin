package com.est.jobshin.domain.interview.dto;

import com.est.jobshin.domain.interview.domain.Interview;
import com.est.jobshin.domain.interviewDetail.domain.InterviewDetail;
import com.est.jobshin.domain.interviewDetail.dto.InterviewDetailDto;
import com.est.jobshin.domain.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** DTO for {@link Interview} */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewDto implements Serializable {
    private Long id;

    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createAt;

    private UserDto user;

    private List<InterviewDetailDto> interviewDetails = new ArrayList<>();

    public static InterviewDto fromInterview(Interview interview) {

        return InterviewDto.builder()
                .id(interview.getId())
                .title(interview.getTitle())
                .createAt(interview.getCreateAt())
//                .interviewDetails(interview.getInterviewDetails()
//                        .stream().map(InterviewDetailDto::fromInterviewDetail)
//                        .collect(Collectors.toList()))
                .build();
    }

    public Interview toInterview() {
        return Interview.builder()
                .id(id)
                .title(title)
                .createAt(createAt)
                .build();
    }
}
