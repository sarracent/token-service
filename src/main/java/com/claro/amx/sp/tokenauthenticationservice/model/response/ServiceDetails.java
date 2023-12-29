package com.claro.amx.sp.tokenauthenticationservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Object containing Service Details")
public class ServiceDetails {
    @Schema(description = "Value indicating whether the server execution was successful. The value 0 is assumed to indicate that there is no processing error.")
    protected String message;

    @Schema(description = "Description about the result of the service execution. It is a descriptive text associated with the resultCode.")
    protected String code;

    @Schema(description = "Description about the result of the level error on service execution. It is a descriptive text associated with the level field (SUCCESS/WARNING/ERROR).")
    protected String level;

    protected String service;
}
