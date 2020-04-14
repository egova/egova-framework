package com.egova.json.databind.std;

import com.egova.json.databind.utils.JsonTokenUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.flagwind.lang.CodeType;
import com.flagwind.reflect.EntityTypeHolder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;

/**
 * boolean解析器
 * 
 * @author chendb
 * @date 2016年12月8日 下午11:34:47
 */
public class CodeTypeJsonDeserializer<E extends CodeType> extends JsonDeserializer<E> {

    private Class<E> clzss;


    public CodeTypeJsonDeserializer(Class<E> clzss) {
        this.clzss = clzss;

    }

    @Override
    public E deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {

        E e = null;
        try {
            String value = JsonTokenUtils.getParseValue(parser,"value");
            if (StringUtils.isEmpty(value)) {
                return e;
            }
            Constructor ctr = clzss.getConstructor(String.class);
            if(ctr!=null){
                e = clzss.getConstructor(String.class).newInstance(value);
            }else {
                e = clzss.newInstance();
                EntityTypeHolder.getEntityType(clzss).getField("value").setValue(e,new Object[]{value});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return e;
    }

}
