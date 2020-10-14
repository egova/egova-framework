package com.egova.json.databind.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.flagwind.commons.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;

/**
 * 自定义时间解析器
 *
 * @author chendb
 * @date 2016年12月8日 下午11:35:36
 */
@SuppressWarnings("deprecation")
public class CustomDateDeseralizer extends UntypedObjectDeserializer {

    /**
     *
     */
    private static final long serialVersionUID = 8457240513909259495L;

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        if (jp.getCurrentTokenId() == JsonTokenId.ID_STRING) {
            // 防止前端日期组件取消日期之后，传递空字符串问题，这里返回null
            if (StringUtils.isBlank(jp.getText())) {
                return null;
            }
            try {
                return DateUtils.parseDate(jp.getText(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
                        "yyyy-MM-dd HH", "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ss");
            } catch (Exception e) {
                return super.deserialize(jp, ctxt);
            }
        } else {
            return super.deserialize(jp, ctxt);
        }
    }
}
