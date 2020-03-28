package com.egova.json.databind;

import com.flagwind.commons.StringUtils;
import com.flagwind.lang.ExtensibleObject;
import com.flagwind.persistent.AssociativeEntry;
import com.flagwind.persistent.AssociativeProvider;
import com.flagwind.persistent.AssociativeProviderFactory;
import com.flagwind.reflect.EntityTypeHolder;
import com.flagwind.reflect.entities.EntityField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;
import java.util.Set;

/**
 * json序列化联想动作执行器
 */
public class JsonAssociativeExecutor {
    private static Log LOG = LogFactory.getLog(JsonAssociativeExecutor.class);


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

    private static void setAssociativeField(JsonAssociative associative,ExtensibleObject obj,EntityField field) {


        String newFieldName = StringUtils.isBlank(associative.name()) ? field.getName() + "Text" : associative.name();
        if (newFieldName.contains("%s")) {
            newFieldName = String.format(newFieldName, field.getName());
        }
        if (newFieldName.contains("${")) {
            newFieldName = newFieldName.replace("${", "#{");
        }
        if (newFieldName.contains("#{")) {
            newFieldName = getExpressionValue(newFieldName,obj,field);
        }
        if (obj.contains(newFieldName)) {
            return;
        }
        AssociativeProvider provider = AssociativeProviderFactory.instance().resolve(associative.source());
        if (provider == null) {
            LOG.error(String.format("没有找到名为%s的AssociativeProvider对象", associative.source()));
            return;
        }
        try {
            Object v = field.getValue(obj, null);
            AssociativeEntry entry = new AssociativeEntry(newFieldName, associative.source(), associative.extras());
            entry.execute(obj, v);
        } catch (Exception ex) {
            LOG.error(String.format("%s对象json 联想序列化%s字段时异常", obj.getClass().getSimpleName(), field.getName()), ex);
        }

    }

    public static Object getAssociativeValue(EntityField field,Object obj) {
        Object v;
        try {
            v = field.getValue(obj, null);
        } catch (Exception ex) {
            LOG.error(String.format("%s对象json 联想序列化%s字段时异常", obj.getClass().getSimpleName(), field.getName()), ex);
            return null;
        }

        Set<JsonAssociative> associatives = field.getRepeatableAnnotations(JsonAssociative.class);

        if (associatives.isEmpty()) {
            associatives = AnnotatedElementUtils.getMergedRepeatableAnnotations(field.getJavaType(), JsonAssociative.class);
        }

        for (JsonAssociative associative : associatives) {

            AssociativeProvider provider = AssociativeProviderFactory.instance().resolve(associative.source());
            if (provider == null) {
                LOG.error(String.format("没有找到名为%s的AssociativeProvider对象", associative.source()));
                return null;
            }
            AssociativeEntry entry = new AssociativeEntry(null, associative.source(), associative.extras());
            return entry.getAssociateValue(v);
        }
        return v;
    }

    public static void execute(ExtensibleObject obj) {
        List<EntityField> fields = EntityTypeHolder.getFields(obj.getClass());
        for (EntityField field : fields) {
            try {
                Set<JsonAssociative> associatives = field.getRepeatableAnnotations(JsonAssociative.class);

                if(associatives.isEmpty()) {
                    associatives = AnnotatedElementUtils.getMergedRepeatableAnnotations(field.getJavaType(), JsonAssociative.class);
                }
                for (JsonAssociative associative : associatives) {
                    setAssociativeField(associative, obj, field);
                }
            } catch (Exception ex) {
                LOG.error(String.format("分析%s对象的%s联想属性出现异常", obj.getClass().getSimpleName(), field.getName()));
                throw ex;
            }
        }
    }
}
