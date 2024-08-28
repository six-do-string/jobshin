package com.est.jobshin.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class QuestionMessage implements Serializable {
    private Long questionId;
    private String question;
}
