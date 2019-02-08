package org.revo.core.base.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static java.util.stream.Collectors.toList;
import static org.revo.core.base.Domain.Role.*;

/**
 * Created by ashraf on 17/04/17.
 */
@Getter
@Setter
public abstract class BaseUser implements UserDetails {
    @JsonProperty(access = READ_ONLY)
    private boolean locked = true;
    @JsonProperty(access = READ_ONLY)
    private boolean enable = false;
    @JsonIgnore
    private String type = "000";

    @JsonProperty(access = READ_ONLY)
    @Override
    public Collection<CustomGrantedAuthority> getAuthorities() {
        List<String> roles = new ArrayList<>();
        if (type.charAt(0) == '1') {
            roles.add(USER.getBuildRole());
        }
        if (type.charAt(1) == '1') {
            roles.add(MEDIA.getBuildRole());
        }
        if (type.charAt(2) == '1') {
            roles.add(ADMIN.getBuildRole());
            roles.add("ROLE_ACTUATOR");
        }
        return roles.stream().map(CustomGrantedAuthority::new).collect(toList());
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return enable;
    }
}