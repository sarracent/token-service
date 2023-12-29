package com.claro.amx.sp.tokenauthenticationservice.business.bo;

import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;

public interface ChannelCredentialsBO {
    ChannelCredentials getChannelCredentials(String channel);

    void removeAll();
}
