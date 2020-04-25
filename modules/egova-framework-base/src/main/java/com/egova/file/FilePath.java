package com.egova.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件路径
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePath {
    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 服务器路径
     */
    private String path;

    /**
     * 网络路径
     */
    private String url;

}

