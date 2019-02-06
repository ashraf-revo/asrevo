package org.revo.core.base.Doamin;

import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    public CustomGrantedAuthority() {
    }

    public CustomGrantedAuthority(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
