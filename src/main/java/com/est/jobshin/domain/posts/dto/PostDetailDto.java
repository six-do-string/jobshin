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
public class PostDetailDto {
    private String title;
    private String content;
    private LocalDateTime latestTime;

    public static PostDetailDto fromPost(Post post) {
        LocalDateTime latestTime;
        if(post.getUpdatedAt() == null) latestTime = post.getCreatedAt();
        else latestTime = post.getUpdatedAt();
        return PostDetailDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .latestTime(latestTime)
                .build();
    }
}
