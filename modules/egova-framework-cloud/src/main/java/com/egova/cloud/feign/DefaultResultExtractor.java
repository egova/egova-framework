package com.egova.cloud.feign;

import com.egova.exception.ExceptionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;

/**
 * 默认提取器
 *
 * @author 奔波儿灞
 * @since 1.0.0
 */
@Slf4j
public class DefaultResultExtractor implements ResultExtractor {

    private static final String HAS_ERROR = "hasError";
    private static final String MESSAGE = "message";
    private static final String RESULT = "result";

    private final ObjectMapper objectMapper;

    DefaultResultExtractor() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String extract(Reader reader) throws IOException {
        JsonNode result = objectMapper.readTree(reader);
        if (result.get(HAS_ERROR).asBoolean()) {
            log.debug("返回结果存在错误: {}", result.get(MESSAGE).textValue());
            throw ExceptionUtils.api(result.get(MESSAGE).textValue());
        }
        JsonNode node = result.get(RESULT);
        if (node.isTextual()) {
            return node.textValue();
        }
        return node.toString();
    }
}
