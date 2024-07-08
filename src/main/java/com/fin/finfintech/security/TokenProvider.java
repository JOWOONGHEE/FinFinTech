package com.fin.finfintech.security;

import com.fin.finfintech.service.UserService;
import com.fin.finfintech.dto.Auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 토큰 만료 시간 60분
    private static final String KEY_ROLES = "roles";

    private final UserService userService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    // jwt 생성
    public String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);

        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 토큰 생성 시간
                .setExpiration(expiredDate) // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘, 비밀키
                .compact();
    }

    public Authentication getAuthentication(String jwt) {

        UserDetails userDetails = this.userService.loadUserByEmail(this.getEmail(jwt));

        return new UsernamePasswordAuthenticationToken(userDetails, "",userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return this.parseClaims(token).getSubject();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String s : (ArrayList<String>) this.parseClaims(token).get(KEY_ROLES)) {
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return s;
                }
                @Override
                public String toString() {
                    return s;
                }
            });
        }
        return authorities;
    }

    public boolean validateToken(String token) {
        if(!StringUtils.hasText(token)) return false;

        var claims = this.parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            // TODO
            return e.getClaims();
        }
    }
}