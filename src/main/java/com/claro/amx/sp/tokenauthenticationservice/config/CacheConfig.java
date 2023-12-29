package com.claro.amx.sp.tokenauthenticationservice.config;


import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CacheConfig {
    private final ChannelCredentialsBO channelCredentialsBO;

    @Scheduled(cron = "${CacheConfig.cron}")
    public void clearCacheSchedule() {
        channelCredentialsBO.removeAll();
    }

}
