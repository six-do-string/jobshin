package com.est.jobshin.domain.posts.service;

import com.est.jobshin.domain.posts.domain.Post;
import com.est.jobshin.domain.posts.dto.PostDetailDto;
import com.est.jobshin.domain.posts.dto.PostDto;
import com.est.jobshin.domain.posts.dto.PostSummaryDto;
import com.est.jobshin.domain.posts.repository.PostRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    @Transactional
    public Page<PostSummaryDto> getAllPosts(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAll(pageRequest);
        return posts.map(PostSummaryDto::fromPost);
    }

    public Optional<PostDetailDto> getPostById(Long id) {
        Optional<PostDetailDto> postDetailDto = postRepository.findById(id).map(PostDetailDto::fromPost);
        if(postDetailDto.isEmpty()) {
            throw new NoSuchElementException();
        }
        return postDetailDto;
    }
}
