package com.egova.cloud.oauth2;

import com.egova.security.core.DefaultUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 从认证中心提取用户权限
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        return Optional.ofNullable(super.extractAuthentication(map))
                .map(authentication -> {
                    String name = authentication.getName();
                    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                    Object principal = map.get(CustomAccessTokenConverter.USER_DETAILS);
                    if (principal == null) {
                        principal = name;
                    } else if (principal instanceof Map) {
                        Map userDetails = (Map) principal;
                        String tenantId = (String) userDetails.get("tenantId");
                        String userId = (String) userDetails.get("id");
                        String username = (String) userDetails.get("username");
                        Boolean enabled = (Boolean) userDetails.get("enabled");
                        Boolean accountNonExpired = (Boolean) userDetails.get("accountNonExpired");
                        Boolean credentialsNonExpired = (Boolean) userDetails.get("credentialsNonExpired");
                        Boolean accountNonLocked = (Boolean) userDetails.get("accountNonLocked");
                        principal = new DefaultUserDetails(
                                tenantId, userId, username, "N/A",
                                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                                authorities
                        );
                    }
                    return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
                }).orElse(null);
    }

}
