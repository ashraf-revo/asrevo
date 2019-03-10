package org.revo.core.base.Config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * Detects if there is no {@code Authentication} object in the
 * {@code ReactiveSecurityContextHolder}, and populates it with one if needed.
 *
 * @author Ankur Pathak
 * @since 5.2.0
 */
public class AnonymousAuthenticationWebFilter implements WebFilter {
    // ~ Instance fields
    // ================================================================================================

    private String key;
    private Object principal;
    private List<GrantedAuthority> authorities;

    /**
     * Creates a filter with a principal named "anonymousUser" and the single authority
     * "ROLE_ANONYMOUS".
     *
     * @param key the key to identify tokens created by this filter
     */
    public AnonymousAuthenticationWebFilter(String key) {
        this(key, "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
    }

    /**
     * @param key         key the key to identify tokens created by this filter
     * @param principal   the principal which will be used to represent anonymous users
     * @param authorities the authority list for anonymous users
     */
    public AnonymousAuthenticationWebFilter(String key, Object principal,
                                            List<GrantedAuthority> authorities) {
        Assert.hasLength(key, "key cannot be null or empty");
        Assert.notNull(principal, "Anonymous authentication principal must be set");
        Assert.notNull(authorities, "Anonymous authorities must be set");
        this.key = key;
        this.principal = principal;
        this.authorities = authorities;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> {
                    SecurityContext securityContext = new SecurityContextImpl();
                    securityContext.setAuthentication(createAuthentication(exchange));
                    return chain.filter(exchange)
                            .subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)))
                            .then(Mono.empty());
                })).flatMap(securityContext -> chain.filter(exchange));

    }

    protected Authentication createAuthentication(ServerWebExchange exchange) {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(key,
                principal, authorities);
        return auth;
    }

    private static final String _key = UUID.randomUUID().toString();
    private static Object _principal = "anonymousUser";
    private static List<GrantedAuthority> _authorities = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");

    public static AnonymousAuthenticationWebFilter build() {
        return new AnonymousAuthenticationWebFilter(_key, _principal, _authorities);
    }
}