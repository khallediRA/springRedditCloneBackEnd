package com.example.demo.Repository;

import java.util.Optional;

import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.Vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRep extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
