package com.egova.security;

import com.egova.security.server.config.AuthorizationServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AuthorizationServerAutoConfiguration.class)
public @interface EgovaAuthorizationServer {
}
