package com.claro.amx.sp.tokenauthenticationservice.controller.impl;

import com.claro.amx.sp.tokenauthenticationservice.annotations.auditable.AuditableApi;
import com.claro.amx.sp.tokenauthenticationservice.annotations.auditable.AuditableParamIgnore;
import com.claro.amx.sp.tokenauthenticationservice.annotations.auditable.AuditableReturn;
import com.claro.amx.sp.tokenauthenticationservice.controller.AuthController;
import com.claro.amx.sp.tokenauthenticationservice.model.request.AuthRequest;
import com.claro.amx.sp.tokenauthenticationservice.model.response.AuthResponse;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceDetails;
import com.claro.amx.sp.tokenauthenticationservice.service.AuthService;
import com.claro.amx.sp.tokenauthenticationservice.service.Resilience4jService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.commons.resolver.CustomHeadersResolver.*;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;


@RestController
@AllArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService authService;
    private final Resilience4jService resilience4JService;

    @Override
    @AuditableApi(
            description = "getToken Api",
            parameterIgnore = @AuditableParamIgnore(nameToAudit = "httpHeadersMap", type = Map.class),
            returnMethod = @AuditableReturn(type = AuthResponse.class))
    public ResponseEntity<AuthResponse> getToken(final Map<String, Object> httpHeadersMap) {

        final Map<String, String> headersMap = getHeadersMap(httpHeadersMap);
        validateHeaders(headersMap);

        var request = AuthRequest.builder()
                .channelId(headersMap.get(CHANNEL_NAME))
                .sessionId(headersMap.get(SESSION_NAME))
                .bearerToken(headersMap.get(BEARER_TOKEN))
                .build();
        var response = AuthResponse.builder()
                .token(resilience4JService.executeGetToken(() -> authService.generateToken(request)))
                .serviceDetails(ServiceDetails.builder()
                        .code(ZERO_MSG)
                        .message(OK_MSG)
                        .level(SUCCESS_MSG)
                        .service(SERVICE)
                        .build())
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

}
