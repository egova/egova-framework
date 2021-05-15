package com.egova.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chendb
 * @description: web静态资源
 * @date 2020-04-18 14:26:38
 */
@Deprecated
@Data
@ConfigurationProperties(prefix = "egova.web.static")
public class StaticResourceProperties {


    private String fileDir = "/egova-apps/";


    private String fileSite = "files";

//    private String logSite="logs";
}
