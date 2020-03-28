package com.egova.security.server.jdbc;

//
///**
// * oauth认证token jdbc存储配置(若启动需要增加@Configuration和@EnableAuthorizationServer)
// *
// * @author chendb
// * @date 2016年12月8日 下午5:29:04
// */
////@Configuration
////@EnableAuthorizationServer
////@AutoConfigureAfter(OauthConfig.class)
//@Conditional(JdbcAuthorizationServerConfig.AuthorizationServerCondition.class)
//@Configuration
////@EnableAuthorizationServer
//public class JdbcAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private AuthenticationEntryPoint authenticationEntryPoint;
//
//    @Autowired
//    private JdbcOauthClientDetailsService clientDetailsService;
//
//    @SuppressWarnings("unused")
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private DataSource dataSource;
//
//
//
//    /**
//     * token存储
//     *
//     * @return token存储
//     * @author chendb
//     * @date 2016年12月8日 下午5:30:36
//     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcOauthTokenStore(dataSource);
//    }
//
//    /**
//     * Code授权服务
//     *
//     * @return Code授权服务
//     * @author chendb
//     * @date 2016年12月8日 下午5:31:00
//     */
//    @Bean
//    protected AuthorizationCodeServices authorizationCodeServices() {
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }
//
//    /**
//     * 授权服务入口
//     *
//     * @return 授权服务入口
//     * @author chendb
//     * @date 2016年12月8日 下午5:32:02
//     */
//    @Primary
//    @Bean
//    public AuthorizationServerTokenServices tokenServices() {
//        return new JdbcOauthTokenServices(this.tokenStore(), this.clientDetailsService);
//    }
//
//    /**
//     * Approval 存储
//     *
//     * @return Approval 存储
//     * @author chendb
//     * @date 2016年12月8日 下午5:32:23
//     */
//    @Bean
//    public ApprovalStore approvalStore() {
//        TokenApprovalStore store = new TokenApprovalStore();
//        store.setTokenStore(tokenStore());
//        return store;
//    }
//
//    /**
//     * jdbc token存储处理类
//     *
//     * @return jdbc token存储处理类
//     * @author chendb
//     * @date 2016年12月8日 下午5:33:09
//     */
//    @Bean
//    public JdbcTokenStoreUserApprovalHandler userApprovalHandler() {
//        return new JdbcTokenStoreUserApprovalHandler(tokenStore(), clientDetailsService);
//    }
//
//    /**
//     * AuthorizationServerEndpointsConfigurer配置入口
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
//    {
//
//        endpoints.authorizationCodeServices(authorizationCodeServices()).authenticationManager(authenticationManager)
//                .tokenServices(tokenServices()).userApprovalHandler(userApprovalHandler());
//    }
//
//    /**
//     * ClientDetailsServiceConfigurer配置入口
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception
//    {
//        clients.withClientDetails(clientDetailsService);
//
//        clients.inMemory().withClient("unity-client").secret(passwordEncoder.encode("unity")).authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social", "pki").authorities("ROLE_CLIENT").scopes("read,write").and().withClient("mobile-client").secret(passwordEncoder.encode("mobile")).authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "social", "pki").authorities("ROLE_CLIENT").scopes("read,write");
//    }
//
//
//    /**
//     * AuthorizationServerSecurityConfigurer配置入口
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
//    {
//
//        oauthServer.authenticationEntryPoint(authenticationEntryPoint).realm("app/clients");
//    }
//
//    public static class AuthorizationServerCondition implements Condition
//    {
//        @Override
//        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata)
//        {
//            String storage = context.getEnvironment().getProperty("egova.security.browser.token-storage", "redis");
//            return storage.equalsIgnoreCase("jdbc");
//        }
//    }
//
//}
