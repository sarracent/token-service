package com.claro.amx.sp.tokenauthenticationservice.dao.ccard;

import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelCredentialsRepository extends CrudRepository<ChannelCredentials,String> {
    Optional<ChannelCredentials> findById(String channelId);

}
