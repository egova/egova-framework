package com.egova.security.resource.config;

import com.egova.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * 该类是资源服务器安全配置类与@EnableResourceServer作用相同
 */
@EnableConfigurationProperties(SecurityProperties.class)
@Import({ResourceServerAutoConfiguration.UnityResourceServer.class, ResourceServerAutoConfiguration.MobileResourceServer.class})
public class ResourceServerAutoConfiguration extends ResourceServerConfiguration {
    private int order = 6;


    public ResourceServerAutoConfiguration() {
        super();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 禁用csrf，否则feign调用401
        http.csrf().disable();
        http.headers().frameOptions().disable();
        super.configure(http);
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }


    /**
     * Web端和C端授权配置
     *
     * @author chendb
     * @date 2016年12月8日 下午4:57:56
     */
    public static class UnityResourceServer extends ResourceServerConfigurerAdapter {

        @Autowired
        private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

        @Autowired
        private SecurityProperties securityProperties;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.authorizeRequests().antMatchers("/unity", "/unity/**").hasAnyAuthority("UNITY", "Server", "ROLE_CLIENT").and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().exceptionHandling()
                    .accessDeniedHandler(oAuth2AccessDeniedHandler);
            // @formatter:on
            switch (securityProperties.getXFrameOptions()) {
                case ALLOW_FROM:
                    http.headers().frameOptions().disable();
                    break;
                case SAMEORIGIN:
                    http.headers().frameOptions().sameOrigin();
                    break;
                case DENY:
                    http.headers().frameOptions().deny();
                    break;
            }
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId("egova-resource");
        }
    }


    /**
     * 移动端资源授权配置
     *
     * @author chendebao
     */
    public static class MobileResourceServer extends ResourceServerConfigurerAdapter {

        @Autowired
        private SecurityProperties securityProperties;

        @Autowired
        private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.authorizeRequests().antMatchers("/m", "/m/**").hasAnyAuthority("MOBILE", "Police", "Mobile")// .access("#oauth2.hasScope('read')
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                    .exceptionHandling().accessDeniedHandler(oAuth2AccessDeniedHandler);
            // @formatter:on
            switch (securityProperties.getXFrameOptions()) {
                case ALLOW_FROM:
                    http.headers().frameOptions().disable();
                    break;
                case SAMEORIGIN:
                    http.headers().frameOptions().sameOrigin();
                    break;
                case DENY:
                    http.headers().frameOptions().deny();
                    break;
            }
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId("egova-resource");
        }
    }

}

