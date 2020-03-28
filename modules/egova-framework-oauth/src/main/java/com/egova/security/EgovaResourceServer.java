package com.egova.security;

import com.egova.security.resource.config.ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableResourceServer
@Import(ResourceServerAutoConfiguration.class)
public @interface EgovaResourceServer {
}
