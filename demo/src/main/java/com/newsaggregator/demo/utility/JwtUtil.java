//package com.newsaggregator.demo.utility;
//
//import io.jsonwebtoken.Claims;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jdk.dynalink.beans.StaticClass;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//
//
//public class JwtUtil {
//
//
//    public static String generateToken(String username) {
//
//
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token valid for 10 hours
//                .signWith(SignatureAlgorithm.HS512, "HrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKey".getBytes(StandardCharsets.UTF_8))
//                .compact();
//    }
//
//
//
//    public static boolean ValidateJwtToken(String autorizationHeader) {
//        String token = autorizationHeader.substring(7);
//        Claims claims= Jwts.parser()
//                .setSigningKey("HrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKey".getBytes(StandardCharsets.UTF_8))
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims != null;
//    }
//}

package com.newsaggregator.demo.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // use a strong key and load from config/env in real projects
    private static final String SECRET = "HrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKeyHrudaySeceretKey";

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 10)) // 10 hours
                .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public static boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | ExpiredJwtException ex) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}

