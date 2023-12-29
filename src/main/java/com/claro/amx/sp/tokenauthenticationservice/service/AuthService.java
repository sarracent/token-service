package com.claro.amx.sp.tokenauthenticationservice.service;

import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;

public interface AuthService {
    String generateToken(AuthRequest authRequest);
}
