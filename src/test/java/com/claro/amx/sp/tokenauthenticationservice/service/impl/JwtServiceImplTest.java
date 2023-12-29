package com.claro.amx.sp.tokenauthenticationservice.service.impl;


import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.BusinessException;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import com.claro.amx.sp.tokenauthenticationservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_CREATION_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {
    @Mock
    private ChannelCredentialsBO channelCredentialsBO;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(channelCredentialsBO);
    }

    @Test
    void testValidateToken() {
        //Given
        final Map<String, Object> headersMap = getHeadersMap();
        var request = getRequest(headersMap);
        String token = getToken();

        when(channelCredentialsBO.getChannelCredentials(request.getChannelId())).thenReturn(ChannelCredentials.builder()
                .channelId("PSP-6")
                .password("123")
                .secret("5367566B59703373367639792F423F6528482B4D6251655468576D5A71345050")
                .build());

        // When & Then
        assertDoesNotThrow(() -> jwtService.validateToken(token, request.getChannelId()));
    }

    @Test
    void testGenerateToken() {

        String user = "EXA743655";
        String secret = "5367566B59703373367639792F423F6528482B4D6251655468576D5A71341708";

        // When
        String token = jwtService.generateToken(AuthRequest.builder()
        .sessionId("DPR").channelId("PSP-6").bearerToken(getToken()).build(), user, secret);

        // Then
        assertNotNull(token);
    }

    @Test
    void testExtractAllClaims() {
        // Given
        String token = getToken();
        String secret = "5367566B59703373367639792F423F6528482B4D6251655468576D5A71345050";

        // When
        Claims claims = jwtService.extractAllClaims(token, secret);

        // Then
        assertNotNull(claims);
    }

    @Test
    void testExtractAllClaims_InvalidToken_ThrowsBusinessException() {
        // Given
        String token = "invalidToken";
        String secret = "someSecret";

        // When & Then
        assertThrows(BusinessException.class, () -> jwtService.extractAllClaims(token, secret));
    }

    @Test
    void testExtractAllClaims_ErrorParsing_ThrowsBusinessException() {
        // Given
        String token = getToken();
        String secret = "5367566B59703373367639792F423F6528482B4D6251655468576D5A71341708";

        // When & Then
        assertThrows(BusinessException.class, () -> jwtService.extractAllClaims(token, secret));
    }

    @Test
    void testCreateTokenWithInvalidInputs() {

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            jwtService.generateToken(AuthRequest.builder().channelId("").sessionId("").bearerToken("").build(), "", "");
        });

        assertEquals(ERROR_CREATION_TOKEN.getCode(), exception.getCode());
        assertEquals(ERROR_CREATION_TOKEN.getLevel(), exception.getLevel());
    }

    private Map<String, Object> getHeadersMap() {
        return java.util.Map.of("session-id", "sessionId", "channel-id", "channelId", "Authorization"
                , "Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6IjEyMyIsIlVzZXItSWQiOiJFWEE3NDM2NTUiLCJpYXQiOjE2OTQxMTQ4ODksImV4cCI6MTY5NDExNjY4OX0.nsxUYlkFKjmlFVojpEJtLgxhI-OJiZRx4EHKz-SciRM");
    }

    private static AuthRequest getRequest(Map<String, Object> headersMap) {
        return AuthRequest.builder()
                .channelId((String) headersMap.get(CHANNEL_NAME))
                .sessionId((String) headersMap.get(SESSION_NAME))
                .bearerToken((String) headersMap.get(BEARER_TOKEN))
                .build();
    }

    private String getToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhDODQ3NDciLCJQYXNzd29yZCI6Im1MTE13TTdkVEF0YkFEOEYifQ.3yvwuLpLPylZVa5Zod8SuIZtMElFv5ctnmqOw2RW3oY";
    }
}