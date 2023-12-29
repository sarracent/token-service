package com.claro.amx.sp.tokenauthenticationservice.commons.resolver;

import static org.junit.jupiter.api.Assertions.*;

import com.claro.amx.sp.tokenauthenticationservice.exception.impl.ControllersException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@ExtendWith(MockitoExtension.class)
class CustomHeadersResolverTest {

    @Test
    void getHttpHeaders() {
        HttpHeaders httpHeaders = CustomHeadersResolver.getHttpHeaders(Map.of(CONTENT_TYPE, "Content-Type"));
        assertEquals(CONTENT_TYPE, httpHeaders.getFirst(CONTENT_TYPE));
    }

    @Test
    void validateHeaders_AllHeadersPresent_NoExceptionThrown() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(SESSION_NAME, "sessionValue");
        headersMap.put(CHANNEL_NAME, "channelValue");
        headersMap.put(BEARER_TOKEN, "tokenValue");

        assertDoesNotThrow(() -> CustomHeadersResolver.validateHeaders(headersMap));
    }

    @Test
    void validateHeaders_SomeHeadersEmpty_ExceptionThrown() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(SESSION_NAME, "");
        headersMap.put(CHANNEL_NAME, "");
        headersMap.put(BEARER_TOKEN, "");

        ControllersException exception = assertThrows(ControllersException.class, () -> CustomHeadersResolver.validateHeaders(headersMap));
        assertTrue(exception.getMessage().contains(SESSION_NAME));
    }

}