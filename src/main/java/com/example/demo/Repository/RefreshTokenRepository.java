package com.example.demo.Repository;

import java.lang.StackWalker.Option;
import java.util.Optional;

import com.example.demo.model.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<String> findByToken(String token);

    void deleteByToken(String token);
}
