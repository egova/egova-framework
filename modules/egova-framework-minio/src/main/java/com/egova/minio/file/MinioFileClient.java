package com.egova.minio.file;

import com.egova.exception.ExceptionUtils;
import com.egova.file.FileClient;
import com.egova.file.FilePath;
import com.egova.minio.MinioClientTemplate;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 底层使用minio实现接口 {@link FileClient}
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public class MinioFileClient implements FileClient {

    public static final String BUCKET = "uploadFileClient";

    private final MinioClientTemplate template;

    public MinioFileClient(MinioClientTemplate template) {
        this.template = template;
    }

    @Override
    public <F extends InputStreamSource> FilePath upload(String suffix, F file, String... directories) {
        String object = object();
        FilePath filePath = new FilePath();
        filePath.setName(object);
        filePath.setType(suffix);
        filePath.setPath(object);
        filePath.setUrl(object);
        try {
            template.putObject(BUCKET, filePath.getName(), file.getInputStream());
        } catch (IOException e) {
            throw ExceptionUtils.framework("upload file to minio server failed", e);
        }
        return filePath;
    }

    @Override
    public <F extends InputStreamSource> List<FilePath> upload(String suffix, F[] files, String... directories) {
        return Arrays.stream(files)
                .map(f -> upload(suffix, f, directories))
                .collect(Collectors.toList());
    }

    @Override
    public FilePath upload(MultipartFile file, String... directories) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String object = object();
        FilePath filePath = new FilePath();
        filePath.setName(object);
        filePath.setType(extension);
        filePath.setPath(object);
        filePath.setUrl(object);
        try {
            template.putObject(BUCKET, filePath.getName(), file.getInputStream());
        } catch (IOException e) {
            throw ExceptionUtils.framework("upload file to minio server failed", e);
        }
        return filePath;
    }

    @Override
    public List<FilePath> upload(MultipartFile[] files, String... directories) {
        return Arrays.stream(files)
                .map(f -> upload(f, directories))
                .collect(Collectors.toList());
    }

    @Override
    public FilePath upload(File file, String... directories) {
        String extension = FilenameUtils.getExtension(file.getName());
        String object = object();
        FilePath filePath = new FilePath();
        filePath.setName(object);
        filePath.setType(extension);
        filePath.setPath(object);
        filePath.setUrl(object);
        try {
            template.putObject(BUCKET, filePath.getName(), new FileInputStream(file));
        } catch (IOException e) {
            throw ExceptionUtils.framework("upload file to minio server failed", e);
        }
        return filePath;
    }

    @Override
    public List<FilePath> upload(File[] files, String... directories) {
        return Arrays.stream(files)
                .map(f -> upload(f, directories))
                .collect(Collectors.toList());
    }

    private String object() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // url = http://www.egova.top:1314/xxxxxx/1234.jpg
    @Override
    public FilePath parsePath(String url) {
        return null;
    }

    @Override
    public boolean deleteFile(FilePath filePath) {
        return false;
    }
}
