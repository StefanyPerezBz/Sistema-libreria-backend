package com.repositorio.biblioteca.JWT;

import com.repositorio.biblioteca.POJO.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    //private String secret = "btechdays";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 horas

    // Obtener clave de firma
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        //return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token JWT inválido o manipulado", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username, String role) {
         Map<String, Object> claims = new HashMap<>();
         claims.put("role", role);
         return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
                //.signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
       final String username = extractUsername(token);
       return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
