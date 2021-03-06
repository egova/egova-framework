package com.egova.minio.config;

import com.egova.minio.MinioClientTemplate;
import com.egova.minio.file.MinioFileClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

/**
 * 启动时创建bucket {@link MinioFileClient}
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class MinioUploadFileClientCommandLineRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MinioUploadFileClientCommandLineRunner.class);

    private final MinioClientTemplate template;

    public MinioUploadFileClientCommandLineRunner(MinioClientTemplate template) {
        this.template = template;
    }

    @Override
    public void run(String... args) {
        if (template.bucketExists(MinioFileClient.BUCKET)) {
            LOG.info("minio bucket [{}] exist already.", MinioFileClient.BUCKET);
        } else {
            LOG.info("minio bucket [{}] not exist, then create it.", MinioFileClient.BUCKET);
            template.makeBucket(MinioFileClient.BUCKET);
        }
    }
}
