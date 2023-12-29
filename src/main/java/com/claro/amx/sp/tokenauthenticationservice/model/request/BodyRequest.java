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
@Schema(description = "Object containing BodyRequest")
public class BodyRequest {
    private String name;
}
