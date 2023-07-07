package com.project.taskmanager.task.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {
    private static final String SECRET_KEY = "GSuovz7BIEJLvLCKwfhghwx8tHfK3ee4L2K9/VPHQhI=";

    private Key getSingnInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateSignature(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(getSingnInKey()).build().parseClaimsJws(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSingnInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        UUID userId = UUID.fromString(claims.getSubject());
        List<GrantedAuthority> authorities = new ArrayList<>();
        String role = claims.get("role", String.class);
        if (role != null && !role.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    }
}
