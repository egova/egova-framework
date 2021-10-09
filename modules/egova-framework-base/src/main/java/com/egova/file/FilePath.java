package com.egova.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件路径
 * ************结构说明************
 * url      http://www.egova.top:1314/files/attachment/images/png/2020041392712.png
 * path     /files/attachment/images/png/2020041392712.png
 * type     png
 * name     2020041392712.png
 * ************结构说明************
 *
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
     * 文件大小（KB）
     */
    private Long size;

    /**
     * 服务器路径(或相对路径)
     */
    private String path;

    /**
     * 网络路径
     */
    private String url;

}

