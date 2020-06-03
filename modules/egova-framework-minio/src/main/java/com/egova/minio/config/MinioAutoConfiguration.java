package com.egova.minio.config;

import com.egova.file.UploadFileClient;
import com.egova.minio.DefaultMinioClientTemplate;
import com.egova.minio.LoadBalancer;
import com.egova.minio.MinioClientLoadBalancer;
import com.egova.minio.MinioClientTemplate;
import com.egova.minio.MinioProperties;
import com.egova.minio.RandomMinioClientRule;
import com.egova.minio.Rule;
import com.egova.minio.file.MinioUploadFileClient;
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
    public UploadFileClient minioUploadFileClient(MinioClientTemplate template) {
        return new MinioUploadFileClient(template);
    }

    @Bean
    public MinioUploadFileClientCommandLineRunner minioUploadFileClientCommandLineRunner(MinioClientTemplate template) {
        return new MinioUploadFileClientCommandLineRunner(template);
    }

}
