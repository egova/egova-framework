package com.egova.minio;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 平台 {@link io.minio.MinioClient} 配置
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@ConfigurationProperties(prefix = MinioProperties.PREFIX)
public class MinioProperties {

    static final String PREFIX = "egova.minio";

    private List<String> endpoints;

    private String accessKey;

    private String secretKey;

    public List<String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

}
