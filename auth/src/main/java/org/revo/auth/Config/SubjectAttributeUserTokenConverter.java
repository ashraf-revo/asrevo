package org.revo.auth.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.LinkedHashMap;
import java.util.Map;

class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter {
    ObjectMapper oMapper = new ObjectMapper();

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
/*
        User principal = (User) authentication.getPrincipal();
        Map<String, Object> map = oMapper.convertValue(principal, Map.class);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null) response.put(entry.getKey(), entry.getValue());
        }
*/
        response.put("user", authentication.getPrincipal());
        response.put("sub", authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }
}