package com.claro.amx.sp.tokenauthenticationservice.controller;

import com.claro.amx.sp.tokenauthenticationservice.model.response.AuthResponse;
import com.claro.amx.sp.tokenauthenticationservice.model.response.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Constants.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = "Authentication Controller", description = "The Authentication Api")
@Validated
@RequestMapping(path = "/auth")
public interface AuthController {
    
    @GetMapping(value = "/token", consumes = {APPLICATION_JSON_VALUE})
    @Operation(summary = "Get Token", description = "Get Token")
    @Parameters(value = {
            @Parameter(name = SESSION_NAME, in = ParameterIn.HEADER, required = true, description = SESSION_DESCR, schema = @Schema(type = "string")),
            @Parameter(name = CHANNEL_NAME, in = ParameterIn.HEADER, required = true, description = CHANNEL_DESCR, schema = @Schema(type = "string")),
            @Parameter(name = BEARER_TOKEN, in = ParameterIn.HEADER, required = true, description = BEARER_TOKEN_DESCR, schema = @Schema(type = "string")),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = SUCCESS_MSG, content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = BADREQUEST_CODE, description = BADREQUEST_MSG, content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
            @ApiResponse(responseCode = INTERNALSERVER_CODE, description = INTERNALSERVER_MSG, content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceResponse.class))),
    })
    ResponseEntity<AuthResponse> getToken(@RequestHeader Map<String, Object> httpHeadersMap);

}
