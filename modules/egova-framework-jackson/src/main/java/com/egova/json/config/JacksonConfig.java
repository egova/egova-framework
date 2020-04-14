package com.egova.json.config;

import com.egova.json.JacksonJsonMapping;
import com.egova.json.JsonMapping;
import com.egova.json.databind.ObjectMappingCustomer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @description: json自动配置类型定义
 *
 * @author chendb
 * @date 2020-04-14 15:44:31
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = ObjectMappingCustomer.class)
    public ObjectMappingCustomer associativeObjectMapper() {
        return new ObjectMappingCustomer(true);
    }


    @Bean("customObjectMapper")
    @ConditionalOnMissingBean(value = ObjectMappingCustomer.class,name = "customObjectMapper")
    public ObjectMappingCustomer customObjectMapper() {
        return new ObjectMappingCustomer(false);
    }

    @Bean
    public JsonMapping JacksonJsonMapper(ObjectMappingCustomer objectMappingCustomer) {
        return new JacksonJsonMapping(objectMappingCustomer);
    }
}
