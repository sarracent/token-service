package com.claro.amx.sp.tokenauthenticationservice.dao.ccard;

import com.claro.amx.sp.tokenauthenticationservice.exception.impl.DataBaseException;
import com.claro.amx.sp.tokenauthenticationservice.mapper.ccard.ChannelCredentialsMapper;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.SqlSessionFactoryBean;

import java.sql.SQLTimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChannelCredentialsDAOTest {

    @Mock
    private SqlSessionFactoryBean sessionFactoryBean;

    @Mock
    private SqlSession sqlSession;

    @Mock
    private Configuration configuration;

    @Mock
    private ChannelCredentialsMapper channelCredentialsMapper;

    @InjectMocks
    private ChannelCredentialsDAO channelCredentialsDAO;

    @BeforeEach
    public void setUp() throws Exception {
        when(sessionFactoryBean.getObject()).thenReturn(mock(SqlSessionFactory.class));
        when(sessionFactoryBean.getObject().openSession()).thenReturn(sqlSession);
        when(sqlSession.getConfiguration()).thenReturn(configuration);
    }

    @Test
    void testGetChannelCredentialsData_Success() {
        // Setup
        String channelId = "sampleChannelId";
        ChannelCredentials mockCredentials = mock(ChannelCredentials.class);
        when(sqlSession.getMapper(ChannelCredentialsMapper.class)).thenReturn(channelCredentialsMapper);
        when(channelCredentialsMapper.getChannelCredentialsData(channelId)).thenReturn(mockCredentials);

        // Act
        ChannelCredentials result = channelCredentialsDAO.getChannelCredentialsData(channelId);

        // Assert
        assertEquals(mockCredentials, result);
    }

    @Test
    void testGetChannelCredentialsData_ThrowsSQLTimeoutException() {
        // Setup
        String channelId = "sampleChannelId";
        PersistenceException ex = new PersistenceException(new SQLTimeoutException());
        when(sqlSession.getMapper(ChannelCredentialsMapper.class)).thenThrow(ex);

        // Assert & Act
        assertThrows(DataBaseException.class, () -> channelCredentialsDAO.getChannelCredentialsData(channelId));
    }

    @Test
    void testGetChannelCredentialsDataError() {

        DataBaseException thrown = assertThrows(DataBaseException.class, () -> {
            channelCredentialsDAO.getChannelCredentialsData(null);
        });

        assertEquals("200001", thrown.getCode());
        assertTrue(thrown.getMessage().contains("Error al consultar la base de datos CCARD ChannelCredentialsDAO"));
    }

}