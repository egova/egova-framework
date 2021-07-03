package com.egova.associative;


import com.egova.exception.ExceptionUtils;
import com.egova.utils.EntityAnnotationUtils;
import com.flagwind.application.Application;
import com.flagwind.commons.StringUtils;
import com.flagwind.lang.ExtensibleObject;
import com.flagwind.reflect.EntityTypeHolder;
import com.flagwind.reflect.entities.EntityField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * json序列化联想动作执行器
 */
public class AssociativeExecutor {

    private static Log LOG = LogFactory.getLog(AssociativeExecutor.class);


    private static String getExpressionValue(String expression, ExtensibleObject obj, EntityField field) {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("object", obj);
        ctx.setVariable("field", field);
        ctx.setRootObject(field);
        // 创建ExpressionParser解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        // 表达式放置
        Expression exp = parser.parseExpression(expression, new ParserContext() {

            @Override
            public boolean isTemplate() {
                return true;
            }

            @Override
            public String getExpressionPrefix() {
                return "#{";
            }

            @Override
            public String getExpressionSuffix() {
                return "}";
            }
        });
        Object v = exp.getValue(ctx);
        return v == null ? field.getName() + "Text" : v.toString();
    }

    private static String getProviderName(Associative associative) {
        if (StringUtils.isEmpty(associative.providerName())) {
            if (Void.class == associative.providerClass()) {
                return "";
            }
            String name = associative.providerClass().getSimpleName();
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return associative.providerName();
    }

    private static AssociativeProvider getProvider(Associative associative) {
        String name = getProviderName(associative);
        if (Application.contains(name)) {
            return Application.resolve(getProviderName(associative));
        } else {
            return null;
        }
    }

    private static Map.Entry<String, Object> getAssociativeField(Associative associative, ExtensibleObject obj, EntityField field) {

        String newFieldName = StringUtils.isBlank(associative.name()) ? field.getName() + "Text" : associative.name();
        if (newFieldName.contains("%s")) {
            newFieldName = String.format(newFieldName, field.getName());
        }
        if (newFieldName.contains("${")) {
            newFieldName = newFieldName.replace("${", "#{");
        }
        if (newFieldName.contains("#{")) {
            newFieldName = getExpressionValue(newFieldName, obj, field);
        }
        if (obj.contains(newFieldName)) {
            return null;
        }
        AssociativeProvider provider = getProvider(associative);
        if (provider == null) {
            String message = String.format("没有找到字段%s上的联想注解的AssociativeProvider", getProviderName(associative));
            if (associative.required()) {
                throw ExceptionUtils.framework(message);
            }
            LOG.warn(message);
            return null;
        }
        try {
            Object v = field.getValue(obj, null);
            Object value;

            if (org.apache.commons.lang3.StringUtils.isEmpty(associative.extras())) {
                value = provider.associate(v);
            } else {
                value = provider.associate(Arrays.asList(v, associative.extras()).toArray());
            }
            obj.set(newFieldName, value);
            return new AbstractMap.SimpleEntry(newFieldName, value);
        } catch (Exception ex) {
            LOG.error(String.format("%s对象json 联想序列化%s字段时异常", obj.getClass().getSimpleName(), field.getName()), ex);
        }
        return null;
    }

    /**
     * 获取扩展对象联想后的Map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> getExtrasMap(ExtensibleObject obj) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        if (obj.getExtras() != null && obj.getExtras().size() > 0) {
            stringObjectMap.putAll(Optional.ofNullable(obj.getExtras()).orElse(new HashMap<>()));
        }
        List<EntityField> fields = EntityTypeHolder.getFields(obj.getClass());
        for (EntityField field : fields) {
            try {
                Set<Associative> associativeSet = EntityAnnotationUtils.getMergedRepeatableAnnotations(field, Associative.class);
                for (Associative associative : associativeSet) {
                    Map.Entry<String, Object> kv = getAssociativeField(associative, obj, field);
                    if (kv != null) {
                        stringObjectMap.put(kv.getKey(), kv.getValue());
                    }
                }
            } catch (Exception ex) {
                String message = String.format("分析%s对象的%s联想属性出现异常", obj.getClass().getSimpleName(), field.getName());
                LOG.error(message);
                throw ExceptionUtils.framework(message, ex);
            }
        }
        return stringObjectMap;
    }


    public static Set<String> execute(ExtensibleObject obj) {
        Map<String, Object> extras = getExtrasMap(obj);
        if (extras != null) {
            extras.entrySet().forEach(kv -> obj.set(kv.getKey(), kv.getValue()));
        }
        return extras.keySet();
    }


    public static void main(String[] args) throws InterruptedException {

        Map<String, Object> stringObjectMap1 = new HashMap<>();
        stringObjectMap1.put("dd", "vvv");
        stringObjectMap1.put("ee", "vvv");
        stringObjectMap1.put("vv", "vvv");

       Map<String, Object> stringObjectMap2 = new HashMap<>(stringObjectMap1);


        stringObjectMap1=null;

        System.out.println(stringObjectMap2.size());


//        Thread thread1 = new Thread(() -> {
//            for (int i = 0; i < 100; i++) {
//                try {
//                    if(stringObjectMap1)
//                    stringObjectMap1.put("vv" + i, "vvv");
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Thread thread2 = new Thread(() -> {
//            for (int i = 0; i < 100; i++) {
//                try {
//                    stringObjectMap1 = null;
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Thread thread3 = new Thread(() -> {
//            for (int i = 0; i < 100; i++) {
//                try {
//                    stringObjectMap2.putAll(stringObjectMap1);
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread1.start();
//        thread2.start();
//        thread3.start();

        try {
            Thread.sleep(1000000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
