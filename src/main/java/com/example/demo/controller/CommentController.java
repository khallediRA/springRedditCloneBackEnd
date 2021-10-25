package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.CommentsDTO;

import com.example.demo.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/comments/")
@AllArgsConstructor

public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Object> createComment(@RequestBody CommentsDTO commentsDTO) {
        commentService.save(commentsDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("{postid}")
    public ResponseEntity<List<CommentsDTO>> getAllCommentForPost(@PathVariable Long postid) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllPostComments(postid));
    }

    @GetMapping("/getAllCommentOfUser/{userid}")
    public ResponseEntity<List<CommentsDTO>> getAllCommentOfUser(@PathVariable Long userid) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentOfUser(userid));

    }

}
