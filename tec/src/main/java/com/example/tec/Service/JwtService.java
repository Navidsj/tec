package com.example.tec.Service;


import com.example.tec.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    public Claims extractClaims(String token) throws Exception {
        return extractAllClaims(token);
    }

    public String generateToken(User userDetails) throws JsonProcessingException {
        return generateToken(new HashMap<>(),userDetails);
    }


    public String extractUsername(String token) throws Exception {
        return extractClaims(token).getSubject();
    }

    public String generateToken(Map<String, Object> extraClaims, User userDetails) throws JsonProcessingException {
        return buildToken(extraClaims,userDetails,jwtExpiration);
    }


    private String buildToken(Map<String,Object> exteraClaims,
                              User userDetails,
                              long expiration) throws JsonProcessingException {


//        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
//        String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(headerJson.getBytes());
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String,Object> m = new HashMap<>();
//        m.put("email",userDetails.getEmail());
//        String payloadJson = objectMapper.writeValueAsString(m);
//        String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes());
//
//        String encodedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(getSignInKey());
//        String jwt = encodedHeader + "." + encodedPayload + "." + encodedSignature;
//        return jwt;

        return Jwts.
                builder().
                setClaims(exteraClaims).
                setSubject(userDetails.getEmail()).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+expiration) ).
                signWith(getSignInKey(), SignatureAlgorithm.HS256).
                compact();

    }

    public boolean isTokenValid(String token,User userDetails) throws Exception {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) throws Exception {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws Exception {
        return extractClaims(token).getExpiration();
    }


    private  Claims extractAllClaims(String token) throws Exception {

        String[] jwtParts = token.split("\\.");

        if (jwtParts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token.");
        }

//        String encodedHeader = jwtParts[0];
//        String encodedPayload = jwtParts[1];
//        String encodedSignature = jwtParts[2];
//
//
//        String headerJson = new String(Base64.getUrlDecoder().decode(encodedHeader), StandardCharsets.UTF_8);
//        String payloadJson = new String(Base64.getUrlDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
//
//        String decodedPayload = new String(Base64.getUrlDecoder().decode(encodedPayload));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> payloadMap = objectMapper.readValue(decodedPayload, Map.class);
//
//        System.out.println(payloadMap);
//
//        return (Claims) payloadMap;

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private static boolean verifySignature(String dataToSign, String encodedSignature, byte[] signingKey) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(signingKey, "HmacSHA256");
        hmac.init(secretKeySpec);

        byte[] signatureBytes = hmac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
        String expectedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);

        return expectedSignature.equals(encodedSignature);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}