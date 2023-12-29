package com.claro.amx.sp.tokenauthenticationservice.mapper.ccard;

import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChannelCredentialsMapper {
    ChannelCredentials getChannelCredentialsData(String channelId);
}