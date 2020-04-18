package com.egova.minio.config;


import com.egova.minio.*;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * minio模块配置
 *
 * @author 奔波儿灞
 * @since 1.0
 */
//@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Rule<MinioClient> randomMinioClientRule() {
        return new RandomMinioClientRule();
    }

    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public LoadBalancer<MinioClient> minioClientLoadBalancer(MinioProperties properties, Rule<MinioClient> minioClientRule) {
        return new MinioClientLoadBalancer(properties, minioClientRule);
    }

    @Bean
    @ConditionalOnMissingBean
    public MinioClientTemplate minioClientTemplate(LoadBalancer<MinioClient> minioClientLoadBalancer) {
        return new DefaultMinioClientTemplate(minioClientLoadBalancer);
    }

}
