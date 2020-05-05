package com.egova.json.databind.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.flagwind.lang.ExtensibleObject;

import java.io.IOException;

/**
 * @author chendb
 * @description: 可扩展对象反序列化
 * @date 2020-05-03 22:08:02
 */
public class ExtensibleObjectDeserializer extends BeanDeserializer {


    public ExtensibleObjectDeserializer(BeanDeserializerBase src) {
        super(src);
    }


    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object bean = super.deserialize(p, ctxt);
        return bean;

    }

    @Override
    protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException {
        JsonToken t = p.currentToken();
        JsonNode jsonNode = p.readValueAs(JsonNode.class);
        Object value;
        switch (t) {
            case VALUE_STRING:
                value = jsonNode.asText();
                break;
            case VALUE_NUMBER_INT:
                value = jsonNode.asInt();
                break;
            case VALUE_NUMBER_FLOAT:
                value = jsonNode.asDouble();
                break;
            case VALUE_TRUE:
            case VALUE_FALSE:
                value = jsonNode.asBoolean();
                break;
            default:
                value = jsonNode;
        }
        ExtensibleObject entity = (ExtensibleObject) beanOrClass;
        entity.set(propName, value);

    }

}

