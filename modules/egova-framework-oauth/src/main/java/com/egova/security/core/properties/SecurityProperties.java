package com.egova.security.core.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 * @author chenabao
 */
@ConfigurationProperties(prefix = "egova.security")
public class SecurityProperties {

    private XFrameOptionsHeaderWriter.XFrameOptionsMode XFrameOptions = XFrameOptionsHeaderWriter.XFrameOptionsMode.ALLOW_FROM;

    public XFrameOptionsHeaderWriter.XFrameOptionsMode getXFrameOptions() {
        return XFrameOptions;
    }

    public void setXFrameOptions(XFrameOptionsHeaderWriter.XFrameOptionsMode XFrameOptions) {
        this.XFrameOptions = XFrameOptions;
    }
}
