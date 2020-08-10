package com.egova.security.core;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 默认的用户详情
 */

public class DefaultUserDetails extends org.springframework.security.core.userdetails.User {


    private String tenantId;

    private String id;

    private String personId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public DefaultUserDetails(String tenantId, String userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = userId;
        this.tenantId = tenantId;
    }

    public DefaultUserDetails(String tenantId, String userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = userId;
        this.tenantId = tenantId;
    }
}
