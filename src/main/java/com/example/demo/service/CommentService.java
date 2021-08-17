package com.example.demo.service;

import java.util.List;

import java.util.stream.Collectors;

import com.example.demo.Repository.CommentRepo;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepo;
import com.example.demo.dto.CommentsDTO;
import com.example.demo.exceptions.PostNotFoundException;
import com.example.demo.mapper.CommentRedditMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.Post;
import com.example.demo.model.User;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final PostRepository postRepository;
    private final AuthService authService;

    private final CommentRedditMapper commentRedditMapper;
    private final UserRepo userRepo;

    private final MailContentBuilder mailContentBuilder;
    private final MyMailService myMailService;

    public void save(CommentsDTO commentsDTO) {

        Post post = postRepository.findById(commentsDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDTO.getPostId().toString()));

        User currentUser = authService.getCurrentUser();

        Comment comment = commentRedditMapper.map(commentsDTO, post, currentUser);

        commentRepo.save(comment);

        String message = mailContentBuilder.build(comment.getUser().getUsername() + " posted a comment on your post. ");

        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        myMailService.sendMail(
                new NotificationEmail(user.getUsername() + "commented on your post", user.getEmail(), message));
    }

    public List<CommentsDTO> getAllPostComments(Long postid) {
        Post post = postRepository.findById(postid).orElseThrow(() -> new PostNotFoundException(postid.toString()));
        return commentRepo.findByPost(post).stream().map(commentRedditMapper::mapTDto).collect(Collectors.toList());

    }

    public List<CommentsDTO> getAllCommentOfUser(Long userid) {
        User user = userRepo.findById(userid).orElseThrow(() -> new UsernameNotFoundException(userid.toString()));
        return commentRepo.findByUser(user).stream().map(commentRedditMapper::mapTDto).collect(Collectors.toList());
    }

}
