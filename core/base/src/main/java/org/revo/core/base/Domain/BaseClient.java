package org.revo.core.base.Domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@Document
@Getter
@Setter
public class BaseClient {
    @Id
    private String id;
    private String userId;
    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String clientId;
    private String clientSecret;
    private Set<String> authorizedGrantTypes = Collections.emptySet();
    private Set<String> registeredRedirectUris = Collections.emptySet();
    private Set<String> scope = Collections.emptySet();
    private Set<String> autoApproveScopes = Collections.emptySet();
    private List<GrantedAuthority> authorities = Collections.emptyList();

    public BaseClient init() {
        this.authorizedGrantTypes = new HashSet<>(Arrays.asList("authorization_code"));
        this.registeredRedirectUris = new HashSet<>(Arrays.asList(""));
        this.scope = new HashSet<>(Arrays.asList("read", "write"));
        this.autoApproveScopes = new HashSet<>(Arrays.asList("read"));
        this.authorities = Collections.emptyList();
        return this;
    }

    public BaseClient() {
    }

    public BaseClient(String id) {
        this.id = id;
    }
}
