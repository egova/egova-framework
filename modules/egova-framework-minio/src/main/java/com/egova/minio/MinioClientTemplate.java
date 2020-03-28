package com.egova.minio;

import io.minio.MinioClient;

import java.io.InputStream;

/**
 * 更好用的 {@link MinioClient} 操作模板
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public interface MinioClientTemplate {

    /**
     * 创建一个存储桶
     *
     * @param bucket 存储桶名称
     */
    void makeBucket(String bucket);

    /**
     * 存储桶是否存在
     *
     * @param bucket 存储桶名称
     * @return 存储桶存在返回true，否则返回false
     */
    boolean bucketExists(String bucket);

    /**
     * 对象是否存在
     *
     * @param bucket 存储桶名称
     * @param object 对象名称
     * @return 对象存在返回true，否则返回false
     */
    boolean objectExists(String bucket, String object);

    /**
     * 上传本地文件，如果服务端存在则会更新
     *
     * @param bucket 存储桶名称
     * @param object 对象名称
     * @param file   本地文件路径
     */
    void putObject(String bucket, String object, String file);

    /**
     * 上传文件流，如果服务端存在则会更新
     *
     * @param bucket      存储桶名称
     * @param object      对象名称
     * @param inputStream 文件流
     */
    void putObject(String bucket, String object, InputStream inputStream);

    /**
     * 上传文件流，如果服务端存在则会更新
     *
     * @param bucket      存储桶名称
     * @param object      对象名称
     * @param inputStream 文件流
     * @param size        字节大小，如果为空，minio则会自己去检测大小，造成开销
     * @see MinioClient#getAvailableSize(Object, int)
     */
    void putObject(String bucket, String object, InputStream inputStream, Long size);

    /**
     * 获取对象流
     *
     * @param bucket 存储桶名称
     * @param object 对象名称
     * @return 对象输入流
     */
    InputStream getObject(String bucket, String object);

    /**
     * 根据给定的offset和length，获取部分对象流
     * 如果offset或者length有一个值为-1，则全量下载
     *
     * @param bucket 存储桶名称
     * @param object 对象名称
     * @param offset 偏移量
     * @param length 读取长度
     * @return 对象输入流
     */
    InputStream getObject(String bucket, String object, long offset, long length);

    /**
     * 删除对象
     *
     * @param bucket 存储桶名称
     * @param object 对象名称
     */
    void removeObject(String bucket, String object);

    /**
     * 删除多个对象
     *
     * @param bucket  存储桶名称
     * @param objects 对象名称
     */
    void removeObjects(String bucket, Iterable<String> objects);

}
