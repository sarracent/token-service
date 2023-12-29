package com.claro.amx.sp.tokenauthenticationservice.model.bo;


import com.claro.amx.sp.tokenauthenticationservice.model.ccard.ChannelCredentials;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomChannelDetails implements UserDetails {

    private String channelId;
    private String password;

    public CustomChannelDetails(ChannelCredentials channelCredentials) {
        this.channelId = channelCredentials.getChannelId();
        this.password = channelCredentials.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return channelId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
