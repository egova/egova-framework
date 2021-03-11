package com.egova.security.server.config;

import com.egova.security.core.authentication.DefaultClientDetailsService;
import com.egova.security.core.crypto.NoneOrBCryptPasswordEncoder;
import com.egova.security.core.provider.ClientDetailsExecutor;
import com.egova.security.core.provider.UserDetailsExecutor;
import com.egova.security.server.authentication.DefaultAuthenticationFailureHandler;
import com.egova.security.server.authentication.DefaultAuthenticationSuccessHandler;
import com.egova.security.server.redis.RedisOauthTokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.vote.ScopeVoter;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 认证相关的扩展点配置。配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 *
 * @author chensoft
 */

@Import({DefaultAuthenticationSuccessHandler.class, DefaultAuthenticationFailureHandler.class})
public class AuthenticationBeanConfig {


    /**
     * redis token存储实现
     *
     * @return redis token存储实现
     * @author chendb
     * @date 2016年12月8日 下午5:47:12
     */
    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    public TokenStore tokenStore(RedisConnectionFactory connectionFactory) {
        RedisOauthTokenStore store = new RedisOauthTokenStore(connectionFactory);
        store.setPrefix("egova:security:");
        return store;
    }

    /**
     * 默认密码处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new NoneOrBCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint entryPoint = new OAuth2AuthenticationEntryPoint();
        entryPoint.setTypeName("Basic");
        entryPoint.setRealmName("app/client");
        return entryPoint;
    }

    @Bean
    public ClientDetailsExecutor clientDetailsExecutor(ApplicationContext applicationContext) {
        return new ClientDetailsExecutor(applicationContext);
    }

    @Bean
    public UserDetailsExecutor userDetailsExecutor(ApplicationContext applicationContext) {
        return new UserDetailsExecutor(applicationContext);
    }

    @Bean
    public DefaultClientDetailsService defaultClientDetailsService(ClientDetailsService clientDetailsService, ClientDetailsExecutor clientDetailsExecutor) {
        return new DefaultClientDetailsService(clientDetailsService, clientDetailsExecutor);
    }


    /**
     * redis ClientDetailsUserDetailsService 实现
     *
     * @return redis ClientDetailsUserDetailsService 实现
     * @author chendb
     * @date 2016年12月8日 下午5:48:12
     */
    @Bean
    @ConditionalOnMissingBean(ClientDetailsUserDetailsService.class)
    public ClientDetailsUserDetailsService clientDetailsUserDetailsService(DefaultClientDetailsService clientDetailsService) {
        return new ClientDetailsUserDetailsService(clientDetailsService);
    }


    /**
     * 访问控制决策器
     *
     * @return 访问控制决策器
     * @author chendb
     * @date 2016年12月8日 下午5:22:22
     */
    @Bean
    public UnanimousBased accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();
        decisionVoters.add(new ScopeVoter());
        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }


}
