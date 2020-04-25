package com.egova.file;

import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author chendb
 * @description: 文件操作客户端
 * @date 2020-04-20 16:53:22
 */
public interface UploadFileClient {

    <F extends InputStreamSource> FilePath upload(String suffix, F file, String... directories);


    <F extends InputStreamSource> List<FilePath> upload(String suffix, F[] files, String... directories);


    FilePath upload(MultipartFile file, String... directories);

    List<FilePath> upload(MultipartFile[] files, String... directories);

    FilePath upload(File file, String... directories);


    List<FilePath> upload(File[] files, String... directories);
}
