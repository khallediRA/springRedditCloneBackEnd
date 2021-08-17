package com.example.demo.service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.Repository.UserRepo;
import com.example.demo.Repository.VerificationTokenRepo;
import com.example.demo.dto.AuthenticationResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exceptions.SpringRedditException;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.security.JWTProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
// Used to Avoid @AUTOWIRED since it is not recommended
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    // toSaveuser in the data base
    private final UserRepo userRep;

    // to save Token in the database
    private final VerificationTokenRepo verifTokenRepo;

    // to Send Mail to confirm when siginin up
    private final MyMailService myMailService;

    private final AuthenticationManager authenticationManager;

    private final JWTProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    public void signup(RegisterRequest registerRequest) {

        // Get data From RegisterRequest and use it to create User
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        // Save User In dataBase
        userRep.save(user);
        String token = generateVerificationToken(user);
        myMailService.sendMail(new NotificationEmail("Please Activate your account ", user.getEmail(),
                "Thank you for Signinup to my Application go here to activiate your shit http://localhost:8080/api/auth/accountVerification/"
                        + token));
    }

    // token Generation
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verifTokenRepo.save(verificationToken);
        return token;

    }

    public void verifyToken(String token) {
        Optional<VerificationToken> verificationToken = verifTokenRepo.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("invalid Token"));
        fetchUserAndEnable(verificationToken.get());

    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRep.findByUsername(username)
                .orElseThrow(() -> new SpringRedditException("Username Not Found with name - " + username));

        user.setEnabled(true);
        userRep.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.println(authenticate);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);
        return AuthenticationResponse.builder().refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .authenticationToken(token).expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername()).build();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {

        UserDetails principle = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRep.findByUsername(principle.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("No user Name was found with " + principle.getUsername()));
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder().refreshToken(refreshTokenRequest.getRefreshToken())
                .authenticationToken(token).expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername()).build();
    }

}
