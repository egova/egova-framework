package com.egova.redis.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.flagwind.commons.StringUtils;
import com.flagwind.lang.CodeType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;

public class CodeTypeSerializer   implements ObjectSerializer, ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        T result = null;
        Object value = parser.parse();
        if (value == null) {
            return null;
        }
        try {
            String v = "";
            if (value instanceof JSONObject) {
                v = ((JSONObject) value).getString("value");
            } else {
                v = value.toString();
            }
            result = getNullableResult(v, (Class<T>) type);
//            result = (T) ((Class) type).getConstructor(String.class).newInstance(v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int getFastMatchToken() {
        return 0;
    }

    @Override
    public void write(JSONSerializer serializer, Object o, Object fieldName, Type fieldType, int features) throws IOException {
        CodeType codeType = (CodeType) o;
        SerializeWriter out = serializer.getWriter();
        out.writeString(codeType.getValue());

    }


    public <E> E getNullableResult(String s, Class<E> type) {
        E e = null;
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        try {
            if (Enum.class.isAssignableFrom(type)) {
                return valueOf(type, s);
            }
            e = type.getConstructor(String.class).newInstance(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

    private static <E> E valueOf(Class<E> type, String s) {
        E[] enumValues = type.getEnumConstants();

        if (Enum.class.isAssignableFrom(type)) {
            E e = Arrays.stream(enumValues).filter(g -> ((CodeType) g).getValue().equalsIgnoreCase(s) || ((CodeType) g).getText().equalsIgnoreCase(s)).findFirst().orElse(null);
            if (e != null) {
                return e;
            }
        }

        for (E e : enumValues) {
            Enum en = (Enum) e;
            if (en.name().equalsIgnoreCase(s)) {
                return e;
            }
            if (Integer.toString(en.ordinal()).equals(s)) {
                return e;
            }
        }

        return null;
    }
}