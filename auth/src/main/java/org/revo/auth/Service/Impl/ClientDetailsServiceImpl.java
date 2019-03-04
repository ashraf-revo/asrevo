package org.revo.auth.Service.Impl;

import org.revo.auth.Service.BaseClientService;
import org.revo.core.base.Domain.BaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {


    @Autowired
    private BaseClientService baseClientService;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return baseClientService.loadClientByClientId(clientId)
                .map(ClientDetailsServiceImpl::cast)
                .orElseThrow(() -> new ClientRegistrationException("not found"));

    }

    private static BaseClientDetails cast(BaseClient baseClient) {
        BaseClientDetails result = new BaseClientDetails();
        result.setClientId(baseClient.getClientId());
        result.setClientSecret(baseClient.getClientSecret());
        result.setAuthorizedGrantTypes(baseClient.getAuthorizedGrantTypes());
        result.setRegisteredRedirectUri(baseClient.getRegisteredRedirectUris());
        result.setScope(baseClient.getScope());
        result.setAutoApproveScopes(baseClient.getAutoApproveScopes());
        return result;
    }
}