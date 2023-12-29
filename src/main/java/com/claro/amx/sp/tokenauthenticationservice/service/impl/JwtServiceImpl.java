package com.claro.amx.sp.tokenauthenticationservice.service.impl;


import com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogService;
import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.BusinessException;
import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import com.claro.amx.sp.tokenauthenticationservice.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_TOKEN_INVALID;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_PARSING_TOKEN;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_CREATION_TOKEN;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${ms}")
    private long ms;
    @Value("${s}")
    private long s;
    @Value("${min}")
    private long min;

    private final ChannelCredentialsBO channelCredentialsBO;

    @Override
    @LogService
    public void validateToken(final String token, final String chanelId) {
        final var channelCredentials = channelCredentialsBO.getChannelCredentials(chanelId);
        Jwts.parserBuilder().setSigningKey(getSignKey(channelCredentials.getSecret())).build().parseClaimsJws(token);
    }


    @Override
    @LogService
    public String generateToken(AuthRequest authRequest, String user, String secret){
        Map<String,Object> claims = Map.of(SESSION_NAME, authRequest.getSessionId(),
                CHANNEL_NAME, authRequest.getChannelId(), USER_NAME, user);
        return createToken(claims, secret);
    }

    @Override
    @LogService
    public Claims extractAllClaims(String token, String secret) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException  e) {
            // Handle signature verification failure (e.g., invalid token)
            throw new BusinessException(
                    ERROR_TOKEN_INVALID.getCode(),
                    String.format(ERROR_TOKEN_INVALID.getMessage(), token),
                    ERROR_TOKEN_INVALID.getLevel());
        } catch (Exception e) {
            // Handle other exceptions that may occur during parsing
            throw new BusinessException(
                    ERROR_PARSING_TOKEN.getCode(),
                    String.format(ERROR_PARSING_TOKEN.getMessage(), token),
                    ERROR_PARSING_TOKEN.getLevel());
        }
    }

    private String createToken(Map<String, Object> claims, String secret) {
        String token = null;
        try {
            token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + ms * s * min))
                    .signWith(getSignKey(secret), SignatureAlgorithm.HS256)
                    .compact();
        } catch (JwtException e) {
            // Handle JWT creation exception
            throw new BusinessException(
                    ERROR_CREATION_TOKEN.getCode(),
                    String.format(ERROR_CREATION_TOKEN.getMessage(), token),
                    ERROR_CREATION_TOKEN.getLevel());
        }
        return token;
    }

    private Key getSignKey(String secret) {
        byte[] keyBytes= secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
