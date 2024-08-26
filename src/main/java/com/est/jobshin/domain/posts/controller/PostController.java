package com.est.jobshin.domain.posts.controller;

import com.est.jobshin.domain.posts.dto.PostDto;
import com.est.jobshin.domain.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto) {
        postService.savePost(postDto);
        return ResponseEntity.ok(postDto);
    }
}
