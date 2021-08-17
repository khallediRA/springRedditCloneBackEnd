package com.example.demo.Repository;

import java.util.List;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);

}
