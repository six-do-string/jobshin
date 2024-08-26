package com.est.jobshin.domain.posts.dto;

import com.est.jobshin.domain.posts.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSummaryDto {
    private String title;
    private LocalDateTime latestTime;

    public static PostSummaryDto fromPost(Post post) {
        LocalDateTime latestTime;
        if(post.getUpdatedAt() == null) latestTime = post.getCreatedAt();
        else latestTime = post.getUpdatedAt();
        return PostSummaryDto.builder()
                .title(post.getTitle())
                .latestTime(latestTime)
                .build();
    }
}
