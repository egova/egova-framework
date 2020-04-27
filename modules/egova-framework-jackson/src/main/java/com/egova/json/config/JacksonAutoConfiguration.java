package com.egova.json.config;

import com.egova.json.JacksonJsonMapping;
import com.egova.json.JsonMapping;
import com.egova.json.databind.ObjectMappingCustomer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

/**
 * @author chendb
 * @description: json自动配置类型定义
 * @date 2020-04-14 15:44:31
 */
//@Configuration
@Order(1)
public class JacksonAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(value = ObjectMappingCustomer.class, name = "enableAssociativeObjectMapping")
    public ObjectMappingCustomer enableAssociativeObjectMapping() {
        return new ObjectMappingCustomer(true);
    }


    @Bean
    @ConditionalOnMissingBean(value = ObjectMappingCustomer.class, name = "disableAssociativeObjectMapping")
    public ObjectMappingCustomer disableAssociativeObjectMapping() {
        return new ObjectMappingCustomer(false);
    }

    @Bean
    public JsonMapping JacksonJsonMapper(ObjectMappingCustomer enableAssociativeObjectMapping, ObjectMappingCustomer disableAssociativeObjectMapping) {
        return new JacksonJsonMapping(enableAssociativeObjectMapping, disableAssociativeObjectMapping);
    }
}
