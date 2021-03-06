package com.example.demo.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Service;

// import javax.annotation.PostConstruct;
// import java.io.IOException;
// import java.io.InputStream;
// import java.security.*;
// import java.security.cert.CertificateException;
// import java.sql.Date;
// import java.time.Instant;
// import com.example.demo.exceptions.SpringRedditException;
// import static java.util.Date.from;
// import static io.jsonwebtoken.Jwts.parser;

// @Service
// public class JWTProvider {

//     private KeyStore keyStore;
//     @Value("${jwt.expiration.time}")
//     private Long jwtExpirationInMillis;

//     @PostConstruct
//     public void init() {
//         try {
//             keyStore = KeyStore.getInstance("JKS");
//             InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
//             keyStore.load(resourceAsStream, "secret".toCharArray());
//         } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
//             throw new SpringRedditException("Exception occurred while loading keystore", e);
//         }

//     }

//     public String generateToken(Authentication authentication) {
//         org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication
//                 .getPrincipal();
//         return Jwts.builder().setSubject(principal.getUsername()).setIssuedAt(from(Instant.now()))
//                 .signWith(getPrivateKey()).setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
//                 .compact();
//     }

//     public String generateTokenWithUserName(String username) {
//         return Jwts.builder().setSubject(username).setIssuedAt(from(Instant.now())).signWith(getPrivateKey())
//                 .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis))).compact();
//     }

//     private PrivateKey getPrivateKey() {
//         try {
//             return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
//         } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
//             throw new SpringRedditException("Exception occured while retrieving public key from keystore", e);
//         }
//     }

//     public boolean validateToken(String jwt) {
//         parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
//         return true;
//     }

//     private PublicKey getPublickey() {
//         try {
//             return keyStore.getCertificate("springblog").getPublicKey();
//         } catch (KeyStoreException e) {
//             throw new SpringRedditException("Exception occured while " + "retrieving public key from keystore", e);
//         }
//     }

//     public String getUsernameFromJwt(String token) {
//         Claims claims = parser().setSigningKey(getPublickey()).parseClaimsJws(token).getBody();

//         return claims.getSubject();
//     }

//     public Long getJwtExpirationInMillis() {
//         return jwtExpirationInMillis;
//     }
// }

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import javax.annotation.PostConstruct;

import com.example.demo.exceptions.SpringRedditException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service

public class JWTProvider {

    private KeyStore keyStore;

    @Value("${jwt.expiration.time}")
    private Long jwtExpirationTime;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new SpringRedditException("Exception occurred while loading keystore", e);
        }

    }

    public String generateToken(Authentication authentication) {

        User principal = (User) authentication.getPrincipal();

        return Jwts.builder().setSubject(principal.getUsername()).signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime))).compact();

    }

    public String generateTokenWithUsername(String username) {
        return Jwts.builder().setSubject(username).signWith(getPrivateKey()).setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationTime))).compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception Occured While Trying to retrieve public key from keyStore", e);
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringRedditException("Exception Occured While Trying To Get Public Key ", e);
        }
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationTime;
    }
}
