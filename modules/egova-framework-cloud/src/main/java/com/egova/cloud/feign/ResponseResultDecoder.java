package com.egova.cloud.feign;


import com.egova.web.rest.ResponseResult;
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
    private final ResultExtractor extractor;

    ResponseResultDecoder(Decoder decoder, ResultExtractor extractor) {
        this.decoder = decoder;
        this.extractor = extractor;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        // 接收类型不是ResponseResult，则拆包
        // 该地方type是原始类型，而response为包装过的数据，拆包时暂时直接操作json
        if (!isResponseResult(type) && isJsonResponse(response)) {
            LOG.debug("返回类型不是ResponseResult，拆包类型: {}", type.getTypeName());
            String json = extractor.extract(response.body().asReader(StandardCharsets.UTF_8));
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
