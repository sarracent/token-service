package com.claro.amx.sp.tokenauthenticationservice.service.impl;

import com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogService;
import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.BusinessException;
import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import com.claro.amx.sp.tokenauthenticationservice.service.AuthService;
import com.claro.amx.sp.tokenauthenticationservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.PASS_WORD;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.USER_NAME;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${secret}")
    private String privateSecret;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ChannelCredentialsBO channelCredentialsBO;

    @Override
    @LogService
    public String generateToken(AuthRequest authRequest) {
        String token = getBearerToken(authRequest.getBearerToken());
        final var sharedSecret = channelCredentialsBO.getChannelCredentials(authRequest.getChannelId()).getSecret();

        String password = jwtService.extractAllClaims(token, sharedSecret).get(PASS_WORD, String.class);
        String user = jwtService.extractAllClaims(token, sharedSecret).get(USER_NAME, String.class);

        validateUserAndPassword(password, user);

        try {
            jwtService.validateToken(token, authRequest.getChannelId());
        } catch (Exception e) {
            throw new BusinessException(
                    ERROR_TOKEN_INVALID.getCode(),
                    String.format(ERROR_TOKEN_INVALID.getMessage(), token),
                    ERROR_TOKEN_INVALID.getLevel());
        }

        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getChannelId(), password));

        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest, user, privateSecret);
        } else {
            throw new BusinessException(
                    ERROR_CHANNEL_NOT_AUTHORIZED.getCode(),
                    String.format(ERROR_CHANNEL_NOT_AUTHORIZED.getMessage(), authRequest.getChannelId()),
                    ERROR_CHANNEL_NOT_AUTHORIZED.getLevel());

        }
    }

    private String getBearerToken(String bearerToken) {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    private void validateUserAndPassword(String password, String user) {
        List<String> messageList = new ArrayList<>();
        if (password.isEmpty())
            messageList.add(PASS_WORD);
        if (user.isEmpty())
            messageList.add(USER_NAME);

        if (!messageList.isEmpty())
            throw new BusinessException(
                    ERROR_TOKEN_CONTENT_REQUIRED.getCode(),
                    String.format(ERROR_TOKEN_CONTENT_REQUIRED.getMessage(), String.join(", ", messageList)),
                    ERROR_TOKEN_CONTENT_REQUIRED.getLevel());
    }
}
