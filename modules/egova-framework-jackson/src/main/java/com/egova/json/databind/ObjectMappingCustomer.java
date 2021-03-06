package com.egova.json.databind;

import com.egova.json.databind.std.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flagwind.lang.CodeType;
import com.flagwind.lang.ExtensibleObject;

/**
 * ObjectMapping 扩展 此类对于非标准json解析做了扩展处理
 *
 * @author chendb
 * @date 2016年12月8日 下午11:30:19
 */
public class ObjectMappingCustomer extends ObjectMapper {

    /**
     *
     */
    private static final long serialVersionUID = 1572846160494272924L;




    /**
     * 构造函数
     *
     * @param enableAssociative 是否启用联想注解
     * @param differenceEnum    对于简单类型是否区分枚举，若为true将取name值，否则为value值
     *
     */
    public ObjectMappingCustomer(boolean enableAssociative, boolean differenceEnum) {
        super();
        // 允许单引号
        this.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 字段和值都加引号
        this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 数字不加引号
        this.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, false);
        this.configure(JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS, true);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 大小写不敏感
        this.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);


        // 设置日期格式
        this.setDateFormat(new CustomDateFormat());

        SimpleModule module = getDefaultSimpleModule(enableAssociative, differenceEnum);
        this.registerModule(module);

        // 解决 jackson2 无法反序列化 LocalDateTime 的问题
        this.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.registerModule(new JavaTimeModule());
        this.registerModule(new Jdk8Module());
    }

    public ObjectMappingCustomer(boolean enableAssociative){
        this(enableAssociative,true);
    }

    public static class DefaultBeanSerializerModifier extends BeanSerializerModifier {

        private boolean enableAssociative;

        private boolean differenceEnum;

        public DefaultBeanSerializerModifier(boolean enableAssociative, boolean differenceEnum) {
            this.enableAssociative = enableAssociative;
            this.differenceEnum = differenceEnum;
        }

        @Override
        public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc,
                                                  JsonSerializer<?> serializer) {

            if (ExtensibleObject.class.isAssignableFrom(beanDesc.getBeanClass())) {
                return new ExtensibleObjectSerializer((BeanSerializerBase) serializer, enableAssociative);
            } else if (CodeType.class.isAssignableFrom(beanDesc.getBeanClass())) {
                return new CodeTypeJsonSerializer(this.differenceEnum);
            }
            return serializer;
        }
    }

    public static class DefaultBeanDeserializerModifier extends BeanDeserializerModifier {


        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc,
                                                      JsonDeserializer<?> deserializer) {
            if (beanDesc.getBeanClass() == Boolean.class) {
                return new BooleanJsonDeserializer(true);
            } else if (beanDesc.getBeanClass() == boolean.class) {
                return new BooleanJsonDeserializer(false);
            } else if (beanDesc.getBeanClass() == java.util.Date.class) {
                return new CustomDateDeseralizer();
            } else if (beanDesc.getBeanClass() == java.sql.Timestamp.class) {
                return new CustomTimestampDeseralizer();
            } else if (CodeType.class.isAssignableFrom(beanDesc.getBeanClass())) {
                return new CodeTypeJsonDeserializer(beanDesc.getBeanClass());
            } else if (ExtensibleObject.class.isAssignableFrom(beanDesc.getBeanClass())) {
                return new ExtensibleObjectDeserializer((BeanDeserializerBase) deserializer);
            } else {
                return deserializer;
            }
        }
    }


    public SimpleModule getDefaultSimpleModule(boolean enableAssociative, boolean differenceEnum) {
        SimpleModule module = new SimpleModule("egova-module");

        module.setDeserializers(new CustomSimpleDeserializers());


        // 定义BaseEntity类型序列化规则
        module.setSerializerModifier(new DefaultBeanSerializerModifier(enableAssociative, differenceEnum));


        // 定义Boolean类型反序列化规则
        module.setDeserializerModifier(new DefaultBeanDeserializerModifier());
        return module;

    }
}
