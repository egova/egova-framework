package com.egova.security.core;

import com.egova.security.core.provider.TokenGranterProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chendb
 * @description:
 * @date 2021-03-06 16:52:24
 */
public class TokenGranterWrapper implements TokenGranter {


    private List<TokenGranter> granters;


    public TokenGranterWrapper(AuthenticationManager authenticationManager, List<TokenGranterProvider> providers, AuthorizationServerEndpointsConfigurer endpoints) {
        this.granters = new ArrayList<>();
        this.granters.add(endpoints.getTokenGranter());
        if (providers != null) {
            for (TokenGranterProvider provider : providers) {
                granters.add(provider.supply(authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory()));
            }
        }
    }

    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        for (TokenGranter granter : granters) {
            OAuth2AccessToken grant = granter.grant(grantType, tokenRequest);
            if (grant != null) {
                return grant;
            }
        }
        return null;
    }

    public void addGranter(TokenGranter tokenGranter) {
        if (tokenGranter == null) {
            throw new IllegalArgumentException("Token granter is null");
        }
        granters.add(tokenGranter);
    }

    public List<TokenGranter> getGranters() {
        return this.granters;
    }
}
