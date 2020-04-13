package com.egova.cache.redis.json;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.spi.Module;
import com.flagwind.lang.CodeType;

/**
 * CodeType序列化与反序列化策略
 */
public class CodeTypeModule implements  Module {

    @Override
    public ObjectDeserializer createDeserializer(ParserConfig parserConfig, Class aClass) {
        if(CodeType.class.isAssignableFrom(aClass)) {
            return new CodeTypeSerializer();
        }
        return null;
    }

    @Override
    public ObjectSerializer createSerializer(SerializeConfig serializeConfig, Class aClass) {
        if(CodeType.class.isAssignableFrom(aClass)) {
            return  new CodeTypeSerializer();
        }
        return null;
    }

}