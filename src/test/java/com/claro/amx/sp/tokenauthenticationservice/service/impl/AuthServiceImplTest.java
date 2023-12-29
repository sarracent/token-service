package com.claro.amx.sp.tokenauthenticationservice.service.impl;


import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.BusinessException;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import com.claro.amx.sp.tokenauthenticationservice.service.AuthService;
import com.claro.amx.sp.tokenauthenticationservice.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private ChannelCredentialsBO channelCredentialsBO;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(authenticationManager, jwtService, channelCredentialsBO);

        ReflectionTestUtils.setField(authService, "privateSecret", "5367566B59703373367639792F423F6528482B4D6251655468576D5A71341708");
    }

    @Test
    void testGenerateToken_Successful() {
        //Mock request
        final Map<String, Object> headersMap = getHeadersMap();
        var request = getRequest(headersMap);

        //Mock claims
        Claims claims = mock(Claims.class);
        when(claims.get("Password", String.class)).thenReturn("123");
        when(claims.get("User-Id", String.class)).thenReturn("EXA743655");

        when(channelCredentialsBO.getChannelCredentials(request.getChannelId())).thenReturn(ChannelCredentials.builder()
                .channelId("PSP-6")
                .password("123")
                .secret("5367566B59703373367639792F423F6528482B4D6251655468576D5A71347437")
                .build());

        when(jwtService.extractAllClaims(any(), any())).thenReturn(claims);

        //Mock Authentication
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

        when(jwtService.generateToken(any(), any(), any())).thenReturn(getToken());

        final String token = authService.generateToken(request);

        assertNotNull(token);
        verify(channelCredentialsBO).getChannelCredentials(any());
        verify(jwtService, times(2)).extractAllClaims(any(), any());
        verify(jwtService).validateToken(any(), any());
        verify(jwtService).generateToken(any(), any(), any());
    }

    @Test
    void testGenerateToken_WhenAuthenticationFails_ThrowsException() {
        //Mock request
        final Map<String, Object> headersMap = getHeadersMap();
        var request = getRequest(headersMap);

        //Mock claims
        Claims claims = mock(Claims.class);
        when(claims.get("Password", String.class)).thenReturn("123");
        when(claims.get("User-Id", String.class)).thenReturn("EXA743655");

        when(channelCredentialsBO.getChannelCredentials(request.getChannelId())).thenReturn(ChannelCredentials.builder()
                .channelId("PSP-6")
                .password("123")
                .secret("5367566B59703373367639792F423F6528482B4D6251655468576D5A71347437")
                .build());

        when(jwtService.extractAllClaims(any(), any())).thenReturn(claims);

        //Mock Authentication
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

        // Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            // When
            authService.generateToken(request);
        });

        assertEquals("100104", thrown.getCode());
    }

    @Test
    void testGenerateToken_WhenUserAndPasswordAreNotValid_ThrowsException() {
        //Mock request
        final Map<String, Object> headersMap = Map.of("Session-id", "DPR", "Channel-id", "PSP-6", "Authorization"
                , "Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6IiAiLCJVc2VyLUlkIjoiICJ9.AcmDGtmMfAarScKBTrdeBuiKdJYrkq01jlpOf1YNBpM");
        var request = getRequest(headersMap);

        //Mock claims
        Claims claims = mock(Claims.class);
        when(claims.get("Password", String.class)).thenReturn("");
        when(claims.get("User-Id", String.class)).thenReturn("");

        when(channelCredentialsBO.getChannelCredentials(request.getChannelId())).thenReturn(ChannelCredentials.builder()
                .channelId("PSP-6")
                .password("123")
                .secret("5367566B59703373367639792F423F6528482B4D6251655468576D5A71347437")
                .build());

        when(jwtService.extractAllClaims(any(), any())).thenReturn(claims);

        // Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            // When
            authService.generateToken(request);
        });

        assertEquals("100103", thrown.getCode());
    }

    @Test
    void testGenerateTokenWithOutWordBearer_Successful() {
        //Mock request
        final Map<String, Object> headersMap = Map.of("Session-id", "DPR", "Channel-id", "PSP-6", "Authorization"
                , "eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhBNzQzNjU1IiwiUGFzc3dvcmQiOiIxMjMifQ.3f4Hx8Jgwoh1uho_9LTu8NCs7O_qk1-aCvSGg1K49WI");
        var request = getRequest(headersMap);

        //Mock claims
        Claims claims = mock(Claims.class);
        when(claims.get("Password", String.class)).thenReturn("123");
        when(claims.get("User-Id", String.class)).thenReturn("EXA743655");

        when(channelCredentialsBO.getChannelCredentials(request.getChannelId())).thenReturn(ChannelCredentials.builder()
                .channelId("PSP-6")
                .password("123")
                .secret("5367566B59703373367639792F423F6528482B4D6251655468576D5A71347437")
                .build());

        when(jwtService.extractAllClaims(any(), any())).thenReturn(claims);

        //Mock Authentication
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

        when(jwtService.generateToken(any(), any(), any())).thenReturn(getToken());

        final String token = authService.generateToken(request);

        assertNotNull(token);
        verify(channelCredentialsBO).getChannelCredentials(any());
        verify(jwtService, times(2)).extractAllClaims(any(), any());
        verify(jwtService).validateToken(any(), any());
        verify(jwtService).generateToken(any(), any(), any());
    }

    private Map<String, Object> getHeadersMap() {
        return java.util.Map.of("Session-id", "DPR", "Channel-id", "PSP-6", "Authorization"
                , "Bearer eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhBNzQzNjU1IiwiUGFzc3dvcmQiOiIxMjMifQ.3f4Hx8Jgwoh1uho_9LTu8NCs7O_qk1-aCvSGg1K49WI");
    }

    private static AuthRequest getRequest(Map<String, Object> headersMap) {
        return AuthRequest.builder()
                .channelId((String) headersMap.get(CHANNEL_NAME))
                .sessionId((String) headersMap.get(SESSION_NAME))
                .bearerToken((String) headersMap.get(BEARER_TOKEN))
                .build();
    }

    private String getToken() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJDaGFubmVsLUlkIjoiUFNQLTYiLCJTZXNzaW9uLUlkIjoiRFBSIiwiVXNlci1JZCI6IkVYQTc0MzY1NSIsImlhdCI6MTY5NDExNDkxOCwiZXhwIjoxNjk0MTE2NzE4fQ.OpCF-8tSFaxKPqOZMGmkCShZG0gl2U8563ZZF8tD4vs";
    }

}