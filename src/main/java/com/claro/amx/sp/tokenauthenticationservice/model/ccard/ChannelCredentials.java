package com.claro.amx.sp.tokenauthenticationservice.model.ccard;

import com.claro.amx.sp.tokenauthenticationservice.constants.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(hidden = true)
/**
 * Tiempo de vida de 300 segundos
 */
@RedisHash(value= Constants.CHANNEL_CREDENTIALS_CACHE, timeToLive = 300)
public class ChannelCredentials {
    @Id
    private String channelId;
    private String password;
    private String secret;
}
