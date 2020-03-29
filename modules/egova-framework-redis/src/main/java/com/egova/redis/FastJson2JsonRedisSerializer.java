package com.egova.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import com.egova.redis.json.CodeTypeModule;
import com.egova.redis.json.CodeTypeSerializer;
import com.flagwind.lang.CodeType;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {



    private static final ParserConfig parserConfig = new ParserConfig();


    private Class<T> clazz;

    private static final   SerializeConfig serializeConfig = new XSerializeConfig();

    static {
        parserConfig.setAutoTypeSupport(true);
        parserConfig.register(new CodeTypeModule());
    }

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    public byte[] serialize(T t) {
        if (t == null) {
            return new byte[0];
        }
        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        return JSON.toJSONBytes(t,serializeConfig, SerializerFeature.WriteDateUseDateFormat,SerializerFeature.WriteClassName);
    }

    public T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }

        try {
            String str = new String(bytes, IOUtils.UTF8);
            if (clazz == null) {
                return JSON.parseObject(str, Object.class, parserConfig, new Feature[0]);
            } else {
                return JSON.parseObject(str, clazz, parserConfig, new Feature[0]);
            }
        } catch (Exception var3) {
            throw new SerializationException("Could not deserialize: " + var3.getMessage(), var3);
        }
    }

    public static class XSerializeConfig extends SerializeConfig {


        private CodeTypeSerializer codeTypeSerializer = new CodeTypeSerializer();

        @Override
        public ObjectSerializer getObjectWriter(Class<?> clazz) {
            if (CodeType.class.isAssignableFrom(clazz)) {
                return codeTypeSerializer;
            }
            return super.getObjectWriter(clazz);
        }
    }

}