package com.egova.cloud.feign;

import com.egova.cloud.api.ApiException;
import com.egova.rest.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * OperateResult拆包
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class ResponseResultDecoder implements Decoder {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseResultDecoder.class);

    private final Decoder decoder;
    private final ObjectMapper objectMapper;

    ResponseResultDecoder(Decoder decoder, ObjectMapper objectMapper) {
        this.decoder = decoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        // 接收类型不是OperateResult，则拆包
        if (!isResponseResult(type) && isJsonResponse(response)) {
            LOG.debug("返回类型不是OperateResult，拆包类型: {}", type.getClass().getName());
            ResponseResult result = objectMapper.readValue(response.body().asReader(StandardCharsets.UTF_8), ResponseResult.class);
            if (result.getHasError()) {
                LOG.debug("返回结果存在错误: {}", result.getMessage());
                throw new ApiException(result.getMessage());
            }
            String json = objectMapper.writeValueAsString(result.getResult());
            response = response.toBuilder().body(json, StandardCharsets.UTF_8).build();
        }
        return decoder.decode(response, type);
    }

    private boolean isResponseResult(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Type rawType = pt.getRawType();
            if (rawType instanceof Class) {
                Class clazz = (Class) rawType;
                return ResponseResult.class.isAssignableFrom(clazz);
            }
        }
        return false;
    }

    private boolean isJsonResponse(Response response) {
        Collection<String> contentTypes = response.headers().get("content-type");
        if (contentTypes == null || contentTypes.isEmpty()) {
            contentTypes = response.headers().get("Content-Type");
            if (contentTypes == null || contentTypes.isEmpty()) {
                // content-type不存在
                return false;
            }
        }
        for (String contentType : contentTypes) {
            if (contentType.startsWith("application/json")) {
                return true;
            }
        }
        return false;
    }

}
