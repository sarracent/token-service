package com.claro.amx.sp.tokenauthenticationservice.service.impl;

import com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogService;
import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.TechnicalException;
import com.claro.amx.sp.tokenauthenticationservice.model.bo.CustomChannelDetails;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND;

@Service
public class ChannelDetailsService implements UserDetailsService {

    @Autowired
    private ChannelCredentialsBO channelCredentialsBO;

    @Override
    @LogService
    public UserDetails loadUserByUsername(String channelId) throws UsernameNotFoundException {
        Optional<ChannelCredentials> credential = Optional.ofNullable(channelCredentialsBO.getChannelCredentials(channelId));
        return credential.map(CustomChannelDetails::new)
                .orElseThrow(() -> new TechnicalException(
                        ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND.getCode(),
                        String.format(ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND.getMessage(), channelId),
                        ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND.getLevel()));
    }

}
