package com.claro.amx.sp.tokenauthenticationservice.service;

import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import io.jsonwebtoken.Claims;

public interface JwtService {
    void validateToken(String token, String chanelId);

    String generateToken(AuthRequest authRequest, String user, String secret);

    Claims extractAllClaims(String token, String secre);
}
