package com.example.tec.Service;


import com.example.tec.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${secret.key}")
    private String secretKey;

    @Getter
    private long jwtExpiration = 60*60*1000;

    // generic
    public Claims extractClaims(String token){
        return extractAllClaims(token);
    }

    public String generateToken(User userDetails) {
        return generateToken(new HashMap<>(),userDetails);
    }


    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) {
        return buildToken(extraClaims,userDetails,jwtExpiration);
    }


    private String buildToken(Map<String,Object> exteraClaims,
                              User userDetails,
                              long expiration) {

        return Jwts.
                builder().
                setClaims(exteraClaims).
                setSubject(userDetails.getEmail()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+expiration) ).
                signWith(getSignInKey(), SignatureAlgorithm.HS256).
                compact();

    }

    public boolean isTokenValid(String token,User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }


    private  Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}