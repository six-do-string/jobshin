package com.est.jobshin.domain.posts.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @NotNull
    private String title;

    @NotNull
    private String content;
}
