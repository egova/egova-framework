package com.egova.minio;

import com.egova.exception.FrameworkException;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 负载均衡的minio客户端，https://github.com/minio/minio-java/issues/759
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class MinioClientLoadBalancer implements LoadBalancer<MinioClient> {

    private static final Logger LOG = LoggerFactory.getLogger(MinioClientLoadBalancer.class);

    /**
     * 客户端
     */
    private List<MinioClient> clients;

    /**
     * 配置
     */
    private final MinioProperties properties;

    /**
     * 负载均衡算法
     */
    private final Rule<MinioClient> rule;

    public MinioClientLoadBalancer(MinioProperties properties, Rule<MinioClient> rule) {
        this.properties = properties;
        this.rule = rule;
    }

    public void init() {
        clients = properties.getEndpoints().stream()
                .map(endpoint -> {
            try {
                LOG.info("prepare init minio client: {}", endpoint);
                return new MinioClient(endpoint, properties.getAccessKey(), properties.getSecretKey());
            } catch (Exception e) {
                throw new FrameworkException(e);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public MinioClient get() {
        return Optional.ofNullable(rule.choose(clients))
                .orElseThrow(() -> new FrameworkException("minio client unavailable."));
    }

}
