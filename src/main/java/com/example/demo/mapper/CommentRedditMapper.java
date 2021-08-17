package com.example.demo.mapper;

import com.example.demo.dto.CommentsDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentRedditMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDTO.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentsDTO commentsDTO, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "username", expression = "java(comment.getUser().getUsername())")
    // this method will get a comment and map it to dto using the mapping bellow
    CommentsDTO mapTDto(Comment comment);

}
