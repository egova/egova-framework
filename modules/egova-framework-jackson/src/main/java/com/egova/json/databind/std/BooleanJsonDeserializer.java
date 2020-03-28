package com.egova.json.databind.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * boolean解析器
 * 
 * @author chendb
 * @date 2016年12月8日 下午11:34:47
 */
public class BooleanJsonDeserializer extends JsonDeserializer<Boolean> {

    private boolean hasNullValue;

    /**
     * 构造函数
     * 
     * @param hasNullValue 是否有空值
     */
    public BooleanJsonDeserializer(boolean hasNullValue) {
        this.hasNullValue = hasNullValue;

    }

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        if (StringUtils.isBlank(p.getText())) {
            return hasNullValue ? null : false;
        }
        if ("0".equals(p.getText()) || "false".equalsIgnoreCase(p.getText()) || "no".equalsIgnoreCase(p.getText())
                || "n".equalsIgnoreCase(p.getText())) {
            return Boolean.FALSE;
        }
        if ("1".equals(p.getText()) || "true".equalsIgnoreCase(p.getText()) || "yes".equalsIgnoreCase(p.getText())
                || "y".equalsIgnoreCase(p.getText())) {
            return Boolean.TRUE;
        }
        return Boolean.valueOf(p.getText());

    }

}
