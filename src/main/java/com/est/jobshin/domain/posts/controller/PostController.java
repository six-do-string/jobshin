package com.est.jobshin.domain.posts.controller;

import com.est.jobshin.domain.posts.dto.PostDetailDto;
import com.est.jobshin.domain.posts.dto.PostDto;
import com.est.jobshin.domain.posts.dto.PostSummaryDto;
import com.est.jobshin.domain.posts.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
//
//    private final PostService postService;
//
//    @PostMapping
//    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostDto postDto) {
//        postService.savePost(postDto);
//        return ResponseEntity.ok(postDto);
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<PostSummaryDto>> getAllPosts(Pageable pageable) {
//        return ResponseEntity.ok(postService.getAllPosts(pageable));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Optional<PostDetailDto>> getPostById(@PathVariable Long id) {
//        return ResponseEntity.ok(postService.getPostById(id));
//    }
}
