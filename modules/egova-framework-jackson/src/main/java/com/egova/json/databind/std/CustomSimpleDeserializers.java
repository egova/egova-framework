package com.egova.json.databind.std;

import com.egova.json.databind.utils.JsonTokenUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.flagwind.lang.CodeType;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义枚举解析器用于特定类型json解析处理
 * 
 * @author chendb
 * @date 2016年12月8日 下午11:29:21
 */
public class CustomSimpleDeserializers extends SimpleDeserializers {

    private static final long serialVersionUID = -5369268432518521514L;



    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
            BeanDescription beanDesc) {
        return createDeserializer((Class<Enum<?>>) type);
    }

    private JsonDeserializer<?> createDeserializer(Class<Enum<?>> enumCls) {
        Enum<?>[] enumValues = enumCls.getEnumConstants();
        HashMap<String, Enum<?>> map = createEnumValuesMap(enumValues);
        return new EnumDeserializerExt(new EnumCaseInsensitiveResolver(enumCls, enumValues, map));
    }

    private HashMap<String, Enum<?>> createEnumValuesMap(Enum<?>[] enumValues) {
        HashMap<String, Enum<?>> map = new HashMap<>();
        // from last to first, so that in case of duplicate values, first wins
        for (int i = enumValues.length; --i >= 0;) {
            Enum<?> e = enumValues[i];
            map.put(e.toString(), e);

            if (!e.toString().toLowerCase().equals(e.toString())) {
                map.put(e.toString().toLowerCase(), e);
            }

            if (e instanceof CodeType) {
                CodeType element = (CodeType) e;
                map.put(element.getValue(), e);
                map.put(element.getText(), e);
            }
        }
        return map;
    }

    /**
     * EnumCaseInsensitiveResolver
     * 
     * @author chendb
     * @date 2016年12月9日 上午12:52:01
     */
    public static class EnumCaseInsensitiveResolver extends EnumResolver {

        private static final long serialVersionUID = -5955394013467133584L;

        protected EnumCaseInsensitiveResolver(Class<Enum<?>> enumClass, Enum<?>[] enums, HashMap<String, Enum<?>> map) {
            super(enumClass, enums, map,null);

        }

        @Override
        public Enum<?> findEnum(String key) {
            for (Map.Entry<String, Enum<?>> entry : _enumsById.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    return entry.getValue();
                }
                if (entry instanceof CodeType) {
                    CodeType element = (CodeType) entry;
                    if (element.getValue().equalsIgnoreCase(key)) {
                        return entry.getValue();
                    }
                    if (element.getText().equalsIgnoreCase(key)) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }
    }

    /**
    * 枚举解析器扩展
    * 
    * @author chendb
    * @date 2016年12月8日 下午11:36:35
    */
    public static class EnumDeserializerExt extends EnumDeserializer {

        /**
         *
         */
        private static final long serialVersionUID = 2505416583804016399L;

        private EnumCaseInsensitiveResolver resolver;

        /**
         * 构造函数
         *
         * @param res 枚举解析器
         */
        public EnumDeserializerExt(EnumCaseInsensitiveResolver res) {
            super(res, true);
            this.resolver = res;
            // TODO Auto-generated constructor stub
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException {

            String value = JsonTokenUtils.getParseValue(parser, "value");
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            Object result = this.resolver.findEnum(value);
            if (result != null) {
                return result;
            }
            return super.deserialize(parser, ctxt);

        }

    }
}
