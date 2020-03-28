package com.egova.security.server.redis;//package com.egova.security.server.redis;
//
//import com.egova.security.core.authentication.DefaultClientDetailsService;
//import com.egova.security.core.provider.ClientDetailsProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.*;
//import org.springframework.core.type.AnnotatedTypeMetadata;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.approval.ApprovalStore;
//import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
//import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
//import java.util.List;
//
///**
// * redis 存储的认证服务配置
// *
// * @author chendb
// * @date 2016年12月8日 下午5:53:06
// */
//@Conditional(RedisAuthorizationServerConfig.AuthorizationServerCondition.class)
//@Configuration
////@EnableAuthorizationServer
//public class RedisAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private AuthenticationEntryPoint authenticationEntryPoint;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private ClientDetailsService clientDetailsService;
//
//
//
//    @Autowired
//    private RedisConnectionFactory connectionFactory;
//
//    @Autowired
//    private UserDetailsService defaultUserDetailsService;
//
//    @Bean
//    public DefaultClientDetailsService defaultClientDetailsService()
//    {
//        return new DefaultClientDetailsService(clientDetailsService);
//    }
//
//    /**
//     * redis token存储实现
//     *
//     * @return redis token存储实现
//     * @author chendb
//     * @date 2016年12月8日 下午5:47:12
//     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new RedisOauthTokenStore(connectionFactory);
//    }
//
//    @Bean
//    @Primary
//    public DefaultTokenServices tokenServices() {
//        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore());
//        defaultTokenServices.setClientDetailsService(defaultClientDetailsService());
//        defaultTokenServices.setTokenEnhancer(tokenEnhancer());
//
//        // 刷新token支持是由ClientDetails里面的grant_type来决定的
//        defaultTokenServices.setSupportRefreshToken(true);
//        defaultTokenServices.setReuseRefreshToken(true);
//
//        return defaultTokenServices;
//    }
//
//
//    /**
//     * redis ApprovalStore实现
//     *
//     * @return redis ApprovalStore实现
//     * @author chendb
//     * @date 2016年12月8日 下午5:47:54
//     */
//    @Bean
//    public ApprovalStore approvalStore() {
//
//        return new RedisTokenApprovalStore(this.tokenStore());
//    }
//
//    /**
//     * redis ClientDetailsUserDetailsService 实现
//     *
//     * @return redis ClientDetailsUserDetailsService 实现
//     * @author chendb
//     * @date 2016年12月8日 下午5:48:12
//     */
//    @Bean
//    public ClientDetailsUserDetailsService clientDetailsUserDetailsService() {
//        return new ClientDetailsUserDetailsService(defaultClientDetailsService());
//    }
//
//    /**
//     * redis UserApprovalHandler实现
//     *
//     * @return redis UserApprovalHandler实现
//     * @author chendb
//     * @date 2016年12月8日 下午5:48:33
//     */
//    @Bean
//    public UserApprovalHandler userApprovalHandler() {
//
//        return new RedisTokenStoreUserApprovalHandler(approvalStore(), clientDetailsService);
//    }
//
//    @Bean
//    public TokenEnhancer tokenEnhancer(){
//        return (accessToken, authentication) ->
//        {
////            // 在此处添加附加信息会与token一起缓存起来，这样可能会使附加信息出现脏数据，所以把它禁用
////			if (accessToken instanceof DefaultOAuth2AccessToken)
////            {
////                DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
////                String grantType = authentication.getOAuth2Request().getGrantType();
////                Map<String, String> para = authentication.getOAuth2Request().getRequestParameters();
////                Map<String, Object> additionalInformation = TokenExtraExecutor.execute(grantType, para);
////                token.setAdditionalInformation(additionalInformation);
////            }
//			return accessToken;
//		};
//    }
//
//
//    /**
//     * AuthorizationServerEndpointsConfigurer配置入口
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
//    {
//
////        endpoints.tokenEnhancer(tokenEnhancer()).tokenStore(tokenStore()).reuseRefreshTokens(true);
//		endpoints.setClientDetailsService(defaultClientDetailsService());
//        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).userApprovalHandler(userApprovalHandler()).authenticationManager(authenticationManager);
////        DefaultTokenServices tokenServices = (DefaultTokenServices) endpoints.getDefaultAuthorizationServerTokenServices();
////        tokenServices.setSupportRefreshToken(true);
////        tokenServices.setReuseRefreshToken(true);
//        //
//    }
//
//
//
//    /**
//     * ClientDetailsServiceConfigurer配置入口
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                    .withClient("unity-client")
//                    .secret(passwordEncoder.encode("unity"))
//                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social","pki")
//                    .authorities("ROLE_CLIENT")
//                    .scopes("read,write")
//                .and().withClient("mobile-client")
//                    .secret(passwordEncoder.encode("mobile"))
//                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social","pki")
//                    .authorities("ROLE_CLIENT")
//                    .scopes("read,write");
//    }
//
//    /**
//     * AuthorizationServerSecurityConfigurer 配置入口
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
//    {
//        oauthServer.allowFormAuthenticationForClients();
//        oauthServer.authenticationEntryPoint(authenticationEntryPoint).realm("app/clients");
//    }
//
//
//
//
//    public static class AuthorizationServerCondition implements Condition
//    {
//        @Override
//        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)
//        {
//            String storage = context.getEnvironment().getProperty("egova.security.browser.token-storage", "redis");
//            return storage.equalsIgnoreCase("redis");
//        }
//    }
//}
