package com.claro.amx.sp.tokenauthenticationservice.dao.ccard;


import com.claro.amx.sp.tokenauthenticationservice.annotations.log.LogDAO;
import com.claro.amx.sp.tokenauthenticationservice.config.MyBatisConfig;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.DataBaseException;
import com.claro.amx.sp.tokenauthenticationservice.mapper.ccard.ChannelCredentialsMapper;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.SQLTimeoutException;
import java.util.Objects;

import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_DATABASE_SP_CHANNEL_CREDENTIALS;
import static com.claro.amx.sp.tokenauthenticationservice.constants.Errors.ERROR_DATABASE_TIMEOUT_CHANNEL_CREDENTIAL;


@Component
@RequiredArgsConstructor
public class ChannelCredentialsDAO {

    @Qualifier(MyBatisConfig.CCARD_SESSION_FACTORY)
    private final SqlSessionFactoryBean sessionFactoryBean;

    @Value("${channel.credentials.dao.timeout}")
    private Integer timeout;

    @LogDAO
    public ChannelCredentials getChannelCredentialsData(String channelId) {
        try (var sqlSession = Objects.requireNonNull(sessionFactoryBean.getObject()).openSession()) {
            sqlSession.getConfiguration().setDefaultStatementTimeout(timeout);
            return sqlSession.getMapper(ChannelCredentialsMapper.class).getChannelCredentialsData(channelId);
        }
        catch (PersistenceException ex) {
            if (ex.getCause() instanceof SQLTimeoutException) {
                throw new DataBaseException(ERROR_DATABASE_TIMEOUT_CHANNEL_CREDENTIAL.getCode(),
                        String.format(ERROR_DATABASE_TIMEOUT_CHANNEL_CREDENTIAL.getMessage(), ex.getMessage()),
                        ERROR_DATABASE_TIMEOUT_CHANNEL_CREDENTIAL.getLevel());
            } else {
                throw new DataBaseException(ERROR_DATABASE_SP_CHANNEL_CREDENTIALS.getCode(),
                        String.format(ERROR_DATABASE_SP_CHANNEL_CREDENTIALS.getMessage(), ex.getMessage()),
                        ERROR_DATABASE_SP_CHANNEL_CREDENTIALS.getLevel());
            }
        }
        catch (Exception e) {
            throw new DataBaseException(ERROR_DATABASE_SP_CHANNEL_CREDENTIALS.getCode(),
                    String.format(ERROR_DATABASE_SP_CHANNEL_CREDENTIALS.getMessage(), e.getClass().getSimpleName(), e.getCause()),
                    ERROR_DATABASE_SP_CHANNEL_CREDENTIALS.getLevel());
        }
    }
}
