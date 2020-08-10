package com.egova.security.server.config;

import com.egova.security.core.authentication.DefaultClientDetailsService;
import com.egova.security.core.properties.AuthenticationProperties;
import com.egova.security.core.properties.SecurityProperties;
import com.egova.security.server.jdbc.JdbcOauthClientDetailsService;
import com.egova.security.server.jdbc.JdbcOauthTokenServices;
import com.egova.security.server.jdbc.JdbcOauthTokenStore;
import com.egova.security.server.jdbc.JdbcTokenStoreUserApprovalHandler;
import com.egova.security.server.redis.RedisTokenApprovalStore;
import com.egova.security.server.redis.RedisTokenStoreUserApprovalHandler;
import com.egova.security.utils.SecurityConfigurationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;


@EnableConfigurationProperties(SecurityProperties.class)
@Import({  AuthenticationBeanConfig.class, AuthorizationServerAutoConfiguration.RedisAuthorizationServerConfig.class, AuthorizationServerAutoConfiguration.JdbcAuthorizationServerConfig.class})
public class AuthorizationServerAutoConfiguration extends AuthorizationServerSecurityConfiguration {

    @Autowired
    private DefaultClientDetailsService clientDetailsService;

    @Autowired
    private SecurityProperties securityProperties;

    public AuthorizationServerAutoConfiguration() {
        super();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        super.configure(http);
        http.setSharedObject(ClientDetailsService.class, clientDetailsService);
        http.headers().frameOptions().disable();

        SecurityConfigurationUtils.configure(http, securityProperties);
    }


    /**
     * redis 存储的认证服务配置
     *
     * @author chendb
     * @date 2016年12月8日 下午5:53:06
     */

    @AutoConfigureAfter(name = {"com.egova.redis.config.RedisAutoConfiguration"})
    @EnableConfigurationProperties(AuthenticationProperties.class)
    @Conditional(RedisAuthorizationServerConfig.AuthorizationServerCondition.class)
    public static class RedisAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationProperties authenticationProperties;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private DefaultClientDetailsService clientDetailsService;

        @Autowired
        private TokenStore tokenStore;


        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            return createTokenServices();
        }

        private DefaultTokenServices createTokenServices() {
            DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
            defaultTokenServices.setTokenStore(tokenStore);
            defaultTokenServices.setClientDetailsService(clientDetailsService);
            defaultTokenServices.setTokenEnhancer(tokenEnhancer());
            defaultTokenServices.setAccessTokenValiditySeconds(authenticationProperties.getAccessTokenValiditySeconds());
            defaultTokenServices.setRefreshTokenValiditySeconds(authenticationProperties.getRefreshTokenValiditySeconds());

            // 刷新token支持是由ClientDetails里面的grant_type来决定的
            defaultTokenServices.setSupportRefreshToken(authenticationProperties.isSupportRefreshToken());
            defaultTokenServices.setReuseRefreshToken(authenticationProperties.isReuseRefreshToken());

            return defaultTokenServices;
        }


        /**
         * redis ApprovalStore实现
         *
         * @return redis ApprovalStore实现
         * @author chendb
         * @date 2016年12月8日 下午5:47:54
         */
        @Bean
        public ApprovalStore approvalStore() {

            return new RedisTokenApprovalStore(this.tokenStore);
        }


        /**
         * redis UserApprovalHandler实现
         *
         * @return redis UserApprovalHandler实现
         * @author chendb
         * @date 2016年12月8日 下午5:48:33
         */
        @Bean
        public UserApprovalHandler userApprovalHandler() {

            return new RedisTokenStoreUserApprovalHandler(approvalStore(), clientDetailsService);
        }

        @Bean
        public TokenEnhancer tokenEnhancer() {
            return (accessToken, authentication) ->
            {
                //            // 在此处添加附加信息会与token一起缓存起来，这样可能会使附加信息出现脏数据，所以把它禁用
                //			if (accessToken instanceof DefaultOAuth2AccessToken)
                //            {
                //                DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
                //                String grantType = authentication.getOAuth2Request().getGrantType();
                //                Map<String, String> para = authentication.getOAuth2Request().getRequestParameters();
                //                Map<String, Object> additionalInformation = TokenExtraExecutor.execute(grantType, para);
                //                token.setAdditionalInformation(additionalInformation);
                //            }
                return accessToken;
            };
        }


        /**
         * AuthorizationServerEndpointsConfigurer配置入口
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

            endpoints.setClientDetailsService(clientDetailsService);
            endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                    // 若不设置此项，在sso client进行token检测时会找不到token(因为它采用的InMemoeryTokenServices)
                    .tokenServices(createTokenServices())
                    .userApprovalHandler(userApprovalHandler())
                    .authenticationManager(authenticationManager);

        }


        /**
         * ClientDetailsServiceConfigurer配置入口
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("unity-client")
                    .secret(passwordEncoder.encode("unity"))
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social", "pki","sms")
                    .authorities("ROLE_CLIENT")
                    .scopes("read", "write")
                    .and().withClient("mobile-client")
                    .secret(passwordEncoder.encode("mobile"))
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social", "pki","sms")
                    .authorities("ROLE_CLIENT")
                    .scopes("read", "write");
        }

        /**
         * AuthorizationServerSecurityConfigurer 配置入口
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
            oauthServer.allowFormAuthenticationForClients();
            oauthServer.authenticationEntryPoint(authenticationEntryPoint).realm("app/clients");
            // 若下面没有加，在sso client 带token访问认证服务器会报403错误
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        }


        public static class AuthorizationServerCondition implements Condition {
            @Override
            public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
                String storage = context.getEnvironment().getProperty("egova.security.browser.token-storage", "redis");
                return storage.equalsIgnoreCase("redis");
            }
        }
    }


    /**
     * oauth认证token jdbc存储配置(若启动需要增加@Configuration和@EnableAuthorizationServer)
     *
     * @author chendb
     * @date 2016年12月8日 下午5:29:04
     */
    @Conditional(JdbcAuthorizationServerConfig.AuthorizationServerCondition.class)
    @Import(JdbcOauthClientDetailsService.class)
    public static class JdbcAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private AuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        private JdbcOauthClientDetailsService clientDetailsService;

        @SuppressWarnings("unused")
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private DataSource dataSource;


        /**
         * token存储
         *
         * @return token存储
         * @author chendb
         * @date 2016年12月8日 下午5:30:36
         */
        @Bean
        public TokenStore tokenStore() {
            return new JdbcOauthTokenStore(dataSource);
        }

        /**
         * Code授权服务
         *
         * @return Code授权服务
         * @author chendb
         * @date 2016年12月8日 下午5:31:00
         */
        @Bean
        protected AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        /**
         * 授权服务入口
         *
         * @return 授权服务入口
         * @author chendb
         * @date 2016年12月8日 下午5:32:02
         */
        @Primary
        @Bean
        public AuthorizationServerTokenServices tokenServices() {
            return new JdbcOauthTokenServices(this.tokenStore(), this.clientDetailsService);
        }

        /**
         * Approval 存储
         *
         * @return Approval 存储
         * @author chendb
         * @date 2016年12月8日 下午5:32:23
         */
        @Bean
        public ApprovalStore approvalStore() {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore());
            return store;
        }

        /**
         * jdbc token存储处理类
         *
         * @return jdbc token存储处理类
         * @author chendb
         * @date 2016年12月8日 下午5:33:09
         */
        @Bean
        public JdbcTokenStoreUserApprovalHandler userApprovalHandler() {
            return new JdbcTokenStoreUserApprovalHandler(tokenStore(), clientDetailsService);
        }

        /**
         * AuthorizationServerEndpointsConfigurer配置入口
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

            endpoints.authorizationCodeServices(authorizationCodeServices()).authenticationManager(authenticationManager)
                    .tokenServices(tokenServices()).userApprovalHandler(userApprovalHandler());
        }

        /**
         * ClientDetailsServiceConfigurer配置入口
         */
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientDetailsService);

            clients.inMemory().withClient("unity-client").secret(passwordEncoder.encode("unity")).authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social", "pki").authorities("ROLE_CLIENT").scopes("read,write").and().withClient("mobile-client").secret(passwordEncoder.encode("mobile")).authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social", "pki").authorities("ROLE_CLIENT").scopes("read,write");
        }


        /**
         * AuthorizationServerSecurityConfigurer配置入口
         */
        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) {

            oauthServer.authenticationEntryPoint(authenticationEntryPoint).realm("app/clients");
            // 若下面没有加，在sso client 带token访问认证服务器会报403错误
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        }

        public static class AuthorizationServerCondition implements Condition {
            @Override
            public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
                String storage = context.getEnvironment().getProperty("egova.security.browser.token-storage", "redis");
                return storage.equalsIgnoreCase("jdbc");
            }
        }

    }


}
