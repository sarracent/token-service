package com.claro.amx.sp.tokenauthenticationservice.business.bo.impl;


import com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogBO;
import com.claro.amx.sp.tokenauthenticationservice.business.bo.ChannelCredentialsBO;
import com.claro.amx.sp.tokenauthenticationservice.dao.ccard.ChannelCredentialsDAO;
import com.claro.amx.sp.tokenauthenticationservice.dao.ccard.ChannelCredentialsRepository;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.TechnicalException;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import com.claro.amx.sp.tokenauthenticationservice.utility.Util;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_VALUE;


@Component
@AllArgsConstructor
public class ChannelCredentialsBOImpl implements ChannelCredentialsBO {

    private final ChannelCredentialsDAO channelCredentialsDAO;
    @Autowired
    private ChannelCredentialsRepository channelCredentialsRepository;

    @Override
    @LogBO
    public ChannelCredentials getChannelCredentials(String channelId) {
        //busco en la cache
        var channel =channelCredentialsRepository.findById(channelId);
        //si existe devuelvo el obj
        if(channel.isPresent()) {
            return channel.get();
        }
            final var channelCredentials = channelCredentialsDAO.getChannelCredentialsData(channelId);

        if (Util.isNullOrEmpty(channelCredentials))
            throw new TechnicalException(
                    ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND.getCode(),
                    String.format(ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND.getMessage(), channelId),
                    ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_FOUND.getLevel());
        if (Util.isNullOrEmpty(channelCredentials.getPassword()) || Util.isNullOrEmpty(channelCredentials.getSecret()))
            throw new TechnicalException(
                    ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_VALUE.getCode(),
                    String.format(ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_VALUE.getMessage(), channelId),
                    ERROR_DATABASE_CHANNEL_CREDENTIALS_NOT_VALUE.getLevel());
       //guardo en la cache el resultado traido de la bd.
        channelCredentialsRepository.save(channelCredentials);
        return channelCredentials;

   }

    @Override
    public void removeAll() {
        channelCredentialsRepository.deleteAll();
    }
}
