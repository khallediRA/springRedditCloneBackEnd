package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import java.util.List;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;

import com.example.demo.service.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/posts")
@AllArgsConstructor

public class PostController {
    private final PostService postService;

    @PostMapping("/createpost")
    public ResponseEntity<PostResponse> postMethodName(@RequestBody PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.save(postRequest));

    }

    @GetMapping(value = "/getPostById/{id}")
    public ResponseEntity<PostResponse> getMethodName(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostResponse>> getAllPost() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPost());
    }

}
