package com.claro.amx.sp.tokenauthenticationservice.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Object containing AuthRequest")
public class AuthRequest {

    private String channelId;
    private String sessionId;
    private String bearerToken;

}
