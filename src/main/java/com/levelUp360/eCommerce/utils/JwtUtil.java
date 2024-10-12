package com.levelUp360.eCommerce.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//@Component
//public class JwtUtil {
//
//    private String secretKey = "cF781A";
//
//    public JwtUtil(){
//        KeyGenerator keyGen = null;
//        try {
//            keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String generationToken(String userName){
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, userName);
//    }
//
//    private String createToken(Map<String, Object> claims, String userName) {
//        return Jwts.builder()
//                .claims()
//                .add(claims).subject(userName)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
//                .and()
//                .signWith(getSignKey())
//                .compact();
//    }
//
//    private SecretKey getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String extractUserName(String token){
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
//       final Claims claims = extractAllClaims(token);
//       return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().verifyWith(getSignKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    private Boolean  isTokenExpired(String token){
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaims(token, Claims::getExpiration);
//
//    }
//
//    public Boolean validationToken(String token, UserDetails userDetails){
//        final String userName = extractUserName(token);
//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

//}
