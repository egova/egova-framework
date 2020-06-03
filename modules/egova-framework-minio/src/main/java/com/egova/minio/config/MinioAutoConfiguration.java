package com.egova.minio.config;

import com.egova.file.FileClient;
import com.egova.minio.*;
import com.egova.minio.file.MinioFileClient;
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

    @Bean
    public FileClient minioFileClient(MinioClientTemplate template) {
        return new MinioFileClient(template);
    }

    @Bean
    public MinioUploadFileClientCommandLineRunner minioUploadFileClientCommandLineRunner(MinioClientTemplate template) {
        return new MinioUploadFileClientCommandLineRunner(template);
    }

}
