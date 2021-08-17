package com.example.demo.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.SubRedditRepository;
import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.exceptions.SpringRedditException;
import com.example.demo.exceptions.SubredditNotFoundException;
import com.example.demo.mapper.PostRedditMapper;
import com.example.demo.model.Post;
import com.example.demo.model.SubReddit;
import com.example.demo.model.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;

    private final PostRedditMapper postRedditMapper;

    private final SubRedditRepository subRedditRepository;

    private final AuthService authService;

    public PostResponse save(PostRequest postRequest) {

        SubReddit subReddit = subRedditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(
                        "No SubReddit was found with name " + postRequest.getSubredditName()));
        User currentUser = authService.getCurrentUser();

        Post post = postRepository.save(postRedditMapper.map(postRequest, subReddit, currentUser));
        return postRedditMapper.mapToDto(post);

    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("Post not found with id " + id));
        return postRedditMapper.mapToDto(post);

    }

    public List<PostResponse> getAllPost() {
        return postRepository.findAll().stream().map(postRedditMapper::mapToDto).collect(Collectors.toList());
    }

}
