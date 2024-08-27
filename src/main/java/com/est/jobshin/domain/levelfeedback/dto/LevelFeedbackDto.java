package com.est.jobshin.domain.levelfeedback.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LevelFeedbackDto {

    private Long id;
    private Long userId;
    private String content;
    private int rating;
}