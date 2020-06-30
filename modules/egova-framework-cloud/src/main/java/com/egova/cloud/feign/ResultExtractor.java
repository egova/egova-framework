package com.egova.cloud.feign;

import java.io.IOException;
import java.io.Reader;

/**
 * feign拆包时，结果提取
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
public interface ResultExtractor {

    String extract(Reader reader) throws IOException;

}
