package com.egova.file;

import java.io.File;

/**
 * @description: 文件操作客户端
 *
 * @author chendb
 * @date 2020-04-20 16:53:22
 */
public interface FileClient {

    FilePath upload(String fileType, File file, String... directories);

    FilePath upload(File file, String... directories);

    File download(FilePath filePath);

    boolean delete(FilePath filePath);

}
