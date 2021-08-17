package com.example.demo.service;

import java.util.Optional;

import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.VoteRep;
import com.example.demo.dto.VoteDto;
import com.example.demo.exceptions.PostNotFoundException;
import com.example.demo.exceptions.SpringRedditException;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.Vote;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import static com.example.demo.model.VoteType.UPVOTE;;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRep voteRep;

    private final AuthService authService;

    private final PostRepository postRepository;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(voteDto.getPostId().toString()));

        Optional<Vote> voteByPostAndUser = voteRep.findTopByPostAndUserOrderByVoteIdDesc(post,
                authService.getCurrentUser());

        if (voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already " + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRep.save(mapToVote(voteDto, post));
        postRepository.save(post);

    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder().voteType(voteDto.getVoteType()).post(post).user(authService.getCurrentUser()).build();
    }

}
