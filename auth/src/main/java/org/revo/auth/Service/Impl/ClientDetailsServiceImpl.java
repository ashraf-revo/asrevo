package org.revo.auth.Service.Impl;

import org.revo.core.base.Config.Env;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private Env env;
    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails result = new BaseClientDetails();
        result.setClientId(clientId);
        result.setAuthorizedGrantTypes(Collections.singletonList("authorization_code"));
        result.setRegisteredRedirectUri(new HashSet<>(Collections.singletonList(discoveryClient.getInstances("gateway").get(0).getUri() + "/login/oauth2/code/login-client")));
        result.setClientSecret(encoder.encode(clientId));
        result.setScope(Arrays.asList("read", "write"));
        result.setAutoApproveScopes(Collections.singletonList("read"));
        return result;
    }
}
