package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.Repository.SubRedditRepository;
import com.example.demo.dto.SubRedditDto;
import com.example.demo.exceptions.SpringRedditException;
import com.example.demo.mapper.SubRedditMapper;
import com.example.demo.model.SubReddit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;

    private final SubRedditMapper subRedditMapper;

    @Transactional
    public SubRedditDto save(SubRedditDto subRedditDto) {

        // old way to use function to Map SubReddit From SubRedditDTO
        // SubReddit save = subRedditRepository.save(mapSubredditDto(subRedditDto));

        SubReddit save = subRedditRepository.save(subRedditMapper.mapDtoToSubreddit(subRedditDto));
        subRedditDto.setId(save.getId());
        return subRedditDto;
    }

    @Transactional(readOnly = true)
    public List<SubRedditDto> getAllSubreddits() {

        // old way of use function to Map SubRedditDTO FROM SubReddit
        // return subRedditRepository.findAll().stream().map(this::mapToDto)

        return subRedditRepository.findAll().stream().map(subRedditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubRedditDto getSubRedditById(Long id) {
        SubReddit subReddit = subRedditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No Reddit was found with id = " + id));
        return subRedditMapper.mapSubredditToDto(subReddit);
    }

    // old way to map SubReddit from SubRedditDTO

    // private SubReddit mapSubredditDto(SubRedditDto subRedditDto) {
    // return
    // SubReddit.builder().name(subRedditDto.getSubRedditName()).description(subRedditDto.getDescription())
    // .build();
    // }

    // old way to map SubRedditDTO from SubReddit

    // private SubRedditDto mapToDto(SubReddit subReddit) {
    // return
    // SubRedditDto.builder().subRedditName(subReddit.getName()).id(subReddit.getId())
    // .numberOfPosts(subReddit.getPosts().size()).build();
    // }
}
