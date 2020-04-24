package com.egova.file;

import org.springframework.core.io.InputStreamSource;

import java.util.List;

/**
 * @author chendb
 * @description: 文件操作客户端
 * @date 2020-04-20 16:53:22
 */
public interface UploadFileClient {

    <F extends InputStreamSource> FilePath upload(String fileType, F file, String... directories);

    <F extends InputStreamSource> FilePath upload(F file, String... directories);

    <F extends InputStreamSource> List<FilePath> upload(List<F> files, String... directories);

    <F extends InputStreamSource> List<FilePath> upload(String fileType, List<F> files, String... directories);
}
