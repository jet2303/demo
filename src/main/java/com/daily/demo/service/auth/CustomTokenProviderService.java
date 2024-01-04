package com.daily.demo.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.daily.demo.config.security.UserPrincipal;
import com.daily.demo.config.securityConfig.OAuth2Config;
import com.daily.demo.entity.mapping.TokenMapping;
import com.daily.demo.entity.user.Users;
import com.daily.demo.repository.user.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Base64.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomTokenProviderService {

    @Autowired
    private OAuth2Config oAuth2Config;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    public TokenMapping refreshToken(Authentication authentication, String refreshToken) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();

        Date accessTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getAccessTokenExpirationMsec());

        String secretKey = oAuth2Config.getAuth().getTokenSecret();
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key)
                .compact();

        return TokenMapping.builder()
                .userEmail(userPrincipal.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenMapping createToken(Authentication authentication) {

        String email = (String) authentication.getPrincipal();
        Users findUser = userRepository.findByEmail(email).get();

        UserPrincipal userPrincipal = UserPrincipal.create(findUser);

        /////////////////////////////////////////////////////////////////////////////////////////////////////

        Date now = new Date();

        Date accessTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getAccessTokenExpirationMsec());
        Date refreshTokenExpiresIn = new Date(now.getTime() + oAuth2Config.getAuth().getRefreshTokenExpirationMsec());

        String secretKey = oAuth2Config.getAuth().getTokenSecret();

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenMapping.builder()
                .userEmail(userPrincipal.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(oAuth2Config.getAuth().getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public UsernamePasswordAuthenticationToken getAuthenticationById(String token) {
        Long userId = getUserIdFromToken(token);
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        return authentication;
    }

    public UsernamePasswordAuthenticationToken getAuthenticationByEmail(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        return authentication;
    }

    public Long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(oAuth2Config.getAuth().getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        Long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(oAuth2Config.getAuth().getTokenSecret())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            log.error("잘못된 JWT 서명");
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 서명");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지않는 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었음.");
        }
        return false;
    }
}
