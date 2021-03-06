package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.SubRedditDto;
import com.example.demo.service.SubRedditService;

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
@RequestMapping("api/subreddit")
@AllArgsConstructor

public class SubRedditController {

    private final SubRedditService subRedditService;

    @GetMapping()
    public ResponseEntity<List<SubRedditDto>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getAllSubreddits());
    }

    @PostMapping
    public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subRedditService.save(subRedditDto));

    }

    @GetMapping("{id}")
    public ResponseEntity<SubRedditDto> getSubRedditById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getSubRedditById(id));

    }

}
