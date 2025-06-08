//package com.zosh.config;
//
//import java.util.Collection;
//import java.util.Date;
//
//import javax.crypto.SecretKey;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//
//@Service
//public class JwtTokenProvider {
//
//    private final SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
//
//    public String generateToken(Authentication auth) {
//        Date now = new Date();
//        Date expirationDate = new Date(now.getTime() + 86400000); // 1 day expiration
//
//        return Jwts.builder()
//                .setIssuedAt(now)
//                .setExpiration(expirationDate)
//                .claim("email", auth.getName())
//                .claim("authorities", populateAuthorities(auth.getAuthorities()))
//                .signWith(key)
//                .compact();
//    }
//
//    public String getEmailFromJwtToken(String jwt) {
//        if (jwt.startsWith("Bearer ")) {
//            jwt = jwt.substring(7).trim();
//        }
//
//        try {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(jwt)
//                    .getBody();
//
//            return claims.get("email", String.class);
//        } catch (JwtException e) {
//            throw new JwtException("Failed to parse JWT token: " + e.getMessage(), e);
//        }
//    }
//
//    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        StringBuilder auths = new StringBuilder();
//        for (GrantedAuthority authority : authorities) {
//            if (auths.length() > 0) {
//                auths.append(",");
//            }
//            auths.append(authority.getAuthority());
//        }
//        return auths.toString();
//    }
//}
