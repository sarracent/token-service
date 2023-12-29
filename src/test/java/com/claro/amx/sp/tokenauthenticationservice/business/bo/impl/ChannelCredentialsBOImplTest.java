package com.claro.amx.sp.tokenauthenticationservice.business.bo.impl;

import com.claro.amx.sp.tokenauthenticationservice.dao.ccard.ChannelCredentialsDAO;
import com.claro.amx.sp.tokenauthenticationservice.dao.ccard.ChannelCredentialsRepository;
import com.claro.amx.sp.tokenauthenticationservice.exception.impl.TechnicalException;
import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelCredentialsBOImplTest {

    @Mock
    private ChannelCredentialsDAO channelCredentialsDAO;

    @Mock
    private ChannelCredentialsRepository channelCredentialsRepository;

    @InjectMocks
    private ChannelCredentialsBOImpl channelCredentialsBO;

    @Test
    void testGetChannelCredentials_FromCache() {
        // Setup
        String channelId = "sampleChannelId";
        ChannelCredentials mockCredentials = mock(ChannelCredentials.class);
        when(channelCredentialsRepository.findById(channelId)).thenReturn(Optional.of(mockCredentials));

        // Act
        ChannelCredentials result = channelCredentialsBO.getChannelCredentials(channelId);

        // Assert
        assertEquals(mockCredentials, result);
        verify(channelCredentialsDAO, never()).getChannelCredentialsData(any());
    }

    @Test
    void testGetChannelCredentials_FromDAO() {
        // Setup
        String channelId = "sampleChannelId";
        ChannelCredentials mockCredentials = mock(ChannelCredentials.class);
        when(channelCredentialsRepository.findById(channelId)).thenReturn(Optional.empty());
        when(channelCredentialsDAO.getChannelCredentialsData(channelId)).thenReturn(mockCredentials);
        when(mockCredentials.getPassword()).thenReturn("password");
        when(mockCredentials.getSecret()).thenReturn("secret");

        // Act
        ChannelCredentials result = channelCredentialsBO.getChannelCredentials(channelId);

        // Assert
        assertEquals(mockCredentials, result);
        verify(channelCredentialsRepository).save(mockCredentials); // Ensure result was saved in cache
    }

    @Test
    void testGetChannelCredentials_NotFound() {
        // Setup
        String channelId = "sampleChannelId";
        when(channelCredentialsRepository.findById(channelId)).thenReturn(Optional.empty());
        when(channelCredentialsDAO.getChannelCredentialsData(channelId)).thenReturn(null);

        // Assert & Act
        assertThrows(TechnicalException.class, () -> channelCredentialsBO.getChannelCredentials(channelId));
    }

    @Test
    void testRemoveAll() {
        channelCredentialsBO.removeAll();
        verify(channelCredentialsRepository).deleteAll();
    }
}