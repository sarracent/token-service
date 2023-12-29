package com.claro.amx.sp.tokenauthenticationservice.controller.impl;

import com.claro.amx.sp.tokenauthenticationservice.service.AuthService;
import com.claro.amx.sp.tokenauthenticationservice.service.Resilience4jService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerImplTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private Resilience4jService resilience4JService;

    @Autowired
    private AuthControllerImpl authController;

    @Test
    void testGetTokenResponseOK() {
        final Map<String, Object> headersMap = getHeadersMap();
        var response = authController.getToken(headersMap);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200 ,response.getStatusCode().value());
    }

    private Map<String, Object> getHeadersMap() {
        return Map.of("session-id", "sessionId", "channel-id", "channelId", "authorization"
                , "Bearer eyJhbGciOiJIUzI1NiJ9.eyJQYXNzd29yZCI6IjEyMyIsIlVzZXItSWQiOiJFWEE3NDM2NTUiLCJpYXQiOjE2OTQxMTQ4ODksImV4cCI6MTY5NDExNjY4OX0.nsxUYlkFKjmlFVojpEJtLgxhI-OJiZRx4EHKz-SciRM");
    }
}