package com.example.demo.Repository;

import java.util.Optional;

import com.example.demo.model.SubReddit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

    Optional<SubReddit> findByName(String subredditName);

}
