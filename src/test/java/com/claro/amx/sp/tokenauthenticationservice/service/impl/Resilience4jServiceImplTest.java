package com.claro.amx.sp.tokenauthenticationservice.service.impl;

import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import com.claro.amx.sp.tokenauthenticationservice.service.AuthService;
import com.claro.amx.sp.tokenauthenticationservice.service.Resilience4jService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Resilience4jServiceImplTest {
    @Mock
    private AuthService authService;
    private Resilience4jService resilience4jService;

    @BeforeEach
    void setUp() {
        resilience4jService = new Resilience4jServiceImpl();
    }

    @Test
    void testExecuteGetToken() {
        final Map<String, Object> headersMap = getHeadersMap();
        var request = getRequest(headersMap);

        when(authService.generateToken(request)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJDaGFubmVsLUlkIjoiUFNQLTYiLCJTZXNzaW9uLUlkIjoiRFBSIiwiVXNlci1JZCI6IkVYQTc0MzY1NSIsImlhdCI6MTY5NDExNDkxOCwiZXhwIjoxNjk0MTE2NzE4fQ.OpCF-8tSFaxKPqOZMGmkCShZG0gl2U8563ZZF8tD4vs");

        assertNotNull(resilience4jService.executeGetToken(() -> authService.generateToken(request)));
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
}