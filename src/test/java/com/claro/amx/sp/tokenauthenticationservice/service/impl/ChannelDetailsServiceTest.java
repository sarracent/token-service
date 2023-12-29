package com.claro.amx.sp.tokenauthenticationservice.service.impl;

import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.TechnicalException;
import com.claro.amx.sp.tokenauthenticationservice.model.bo.CustomChannelDetails;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class ChannelDetailsServiceTest {
    @Mock
    private ChannelCredentialsBO channelCredentialsBO;

    @InjectMocks
    private ChannelDetailsService channelDetailsService;

    @Test
    void loadUserByUsername_ChannelFound_ReturnsUserDetails() {
        //Mock request
        final Map<String, Object> headersMap = getHeadersMap();
        var request = getRequest(headersMap);


        when(channelCredentialsBO.getChannelCredentials(request.getChannelId())).thenReturn(ChannelCredentials.builder()
                .channelId("PSP-6")
                .password("123")
                .secret("5367566B59703373367639792F423F6528482B4D6251655468576D5A71347437")
                .build());

        UserDetails result = channelDetailsService.loadUserByUsername(request.getChannelId());

        assertTrue(result instanceof CustomChannelDetails);
    }

    @Test
    void loadUserByUsername_ChannelNotFound_ThrowsTechnicalException() {
        when(channelCredentialsBO.getChannelCredentials(anyString())).thenReturn(null);

        assertThrows(TechnicalException.class, () -> {
            channelDetailsService.loadUserByUsername("someChannelId");
        });

        // You can add more detailed assertions if needed, like checking error codes or messages
    }

    private Map<String, Object> getHeadersMap() {
        return java.util.Map.of("session-id", "sessionId", "channel-id", "channelId", "Authorization"
                , "Bearer eyJhbGciOiJIUzI1NiJ9.eyJVc2VyLUlkIjoiRVhBNzQzNjU1IiwiUGFzc3dvcmQiOiIxMjMifQ.3f4Hx8Jgwoh1uho_9LTu8NCs7O_qk1-aCvSGg1K49WI");
    }

    private static AuthRequest getRequest(Map<String, Object> headersMap) {
        return AuthRequest.builder()
                .channelId((String) headersMap.get(CHANNEL_NAME))
                .sessionId((String) headersMap.get(SESSION_NAME))
                .bearerToken((String) headersMap.get(BEARER_TOKEN))
                .build();
    }
}