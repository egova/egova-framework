package com.egova.minio;

import com.egova.exception.BusinessException;
import io.minio.ErrorCode;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.messages.DeleteError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 默认的 {@link MinioClientTemplate} 实现
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class DefaultMinioClientTemplate implements MinioClientTemplate {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMinioClientTemplate.class);

    private final LoadBalancer<MinioClient> loadBalancer;

    public DefaultMinioClientTemplate(LoadBalancer<MinioClient> loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void makeBucket(String bucket) {
        MinioClient client = loadBalancer.get();
        try {
            client.makeBucket(bucket);
        } catch (Exception e) {
            LOG.error("make bucket {} failed", bucket);
            throw new BusinessException("make bucket failed", e);
        }
    }

    @Override
    public boolean bucketExists(String bucket) {
        MinioClient client = loadBalancer.get();
        try {
            return client.bucketExists(bucket);
        } catch (Exception e) {
            LOG.error("bucket {} exist failed", bucket);
            throw new BusinessException("bucket exist failed", e);
        }
    }

    @Override
    public boolean objectExists(String bucket, String object) {
        MinioClient client = loadBalancer.get();
        try {
            client.statObject(bucket, object);
        } catch (ErrorResponseException e) {
            if (ErrorCode.NO_SUCH_KEY.code().equals(e.errorResponse().code())) {
                return false;
            }
        } catch (Exception e) {
            LOG.error("object object {} failed", object);
            throw new BusinessException("object exist failed", e);
        }
        return true;
    }

    @Override
    public void putObject(String bucket, String object, String file) {
        MinioClient client = loadBalancer.get();
        try {
            client.putObject(bucket, object, file, null, null, null, null);
        } catch (Exception e) {
            LOG.error("put object {} failed", object);
            throw new BusinessException("put object failed", e);
        }
    }

    @Override
    public void putObject(String bucket, String object, InputStream inputStream) {
        putObject(bucket, object, inputStream, null);
    }

    @Override
    public void putObject(String bucket, String object, InputStream inputStream, Long size) {
        MinioClient client = loadBalancer.get();
        try {
            client.putObject(bucket, object, inputStream, size, null, null, null);
        } catch (Exception e) {
            LOG.error("put object {} failed", object);
            throw new BusinessException("put object failed", e);
        }
    }

    @Override
    public InputStream getObject(String bucket, String object) {
        return getObject(bucket, object, -1L, -1L);
    }

    @Override
    public InputStream getObject(String bucket, String object, long offset, long length) {
        MinioClient client = loadBalancer.get();
        try {
            if (offset == -1L || length == -1L) {
                return client.getObject(bucket, object);
            }
            return client.getObject(bucket, object, offset, length);
        } catch (Exception e) {
            LOG.error("get object {} failed", object);
            throw new BusinessException("get object failed", e);
        }
    }

    @Override
    public void removeObject(String bucket, String object) {
        MinioClient client = loadBalancer.get();
        try {
            client.removeObject(bucket, object);
        } catch (Exception e) {
            LOG.error("remove object {} failed", object);
            throw new BusinessException("remove object failed", e);
        }
    }

    @Override
    public void removeObjects(String bucket, Iterable<String> objects) {
        MinioClient client = loadBalancer.get();
        boolean hasError = false;
        try {
            Iterable<Result<DeleteError>> results = client.removeObjects(bucket, objects);
            for (Result<DeleteError> result : results) {
                hasError = true;
                DeleteError error = result.get();
                LOG.error("remove object {} failed, error: {}", error.objectName(), error.message());
            }
        } catch (Exception e) {
            throw new BusinessException("remove objects failed", e);
        }
        if (hasError) {
            throw new BusinessException("remove objects failed");
        }
    }

}
