package com.est.jobshin.domain.posts.service;

import com.est.jobshin.domain.posts.domain.Post;
import com.est.jobshin.domain.posts.dto.PostDetailDto;
import com.est.jobshin.domain.posts.dto.PostDto;
import com.est.jobshin.domain.posts.dto.PostSummaryDto;
import com.est.jobshin.domain.posts.repository.PostRepository;
import com.est.jobshin.domain.user.domain.User;
import com.est.jobshin.domain.user.dto.UserResponse;
import com.est.jobshin.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

//    @Transactional
//    public void savePost(@Valid PostDto postDto) {
//        User user = getCurrentUser();
//        Post post = Post.createPost(postDto.getTitle(), postDto.getContent(), LocalDateTime.now(), user);
//        postRepository.save(post);
//    }
//
//    @Transactional
//    public Page<PostSummaryDto> getAllPosts(Pageable pageable) {
//        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
//        Page<Post> posts = postRepository.findAll(pageRequest);
//        return posts.map(PostSummaryDto::fromPost);
//    }
//
//    @Transactional
//    public Optional<PostDetailDto> getPostById(Long id) {
//        Optional<PostDetailDto> postDetailDto = postRepository.findById(id).map(PostDetailDto::fromPost);
//        if(postDetailDto.isEmpty()) {
//            throw new NoSuchElementException();
//        }
//        return postDetailDto;
//    }
//
//    @Transactional
//    public void updatePost(PostUpdateDto postUpdateDto) {
//        User user = getCurrentUser();
//        Post post = postRepository.findById(postUpdateDto.getId()).orElseThrow(NoSuchElementException::new);
//        if(!post.getUser().getId().equals(user.getId())) throw new NoSuchElementException();
//
//        post.updatePost(postUpdateDto.getTitle(), postUpdateDto.getContent(), LocalDateTime.now());
//    }

//    private User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if(authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
//            throw new RuntimeException();
//        }
//
//        UserResponse principal = (UserResponse) authentication.getPrincipal();
//        return userRepository.findByemail(principal.getEmail())
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//    }
}
