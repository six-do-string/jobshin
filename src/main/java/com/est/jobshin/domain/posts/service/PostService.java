package com.est.jobshin.domain.posts.service;

import com.est.jobshin.domain.posts.domain.Post;
import com.est.jobshin.domain.posts.dto.PostDto;
import com.est.jobshin.domain.posts.repository.PostRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void savePost(@Valid PostDto postDto) {
        Post post = Post.createPost(postDto.getTitle(), postDto.getContent(), LocalDateTime.now());
        postRepository.save(post);
    }
}
