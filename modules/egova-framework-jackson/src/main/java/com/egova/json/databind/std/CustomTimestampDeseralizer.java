package com.egova.json.databind.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * 自定义时间解析器
 * 
 * @author chendb
 * @date 2016年12月8日 下午11:35:36
 */
@SuppressWarnings("deprecation")
public class CustomTimestampDeseralizer extends UntypedObjectDeserializer {

    /**
     * 
     */
    private static final long serialVersionUID = 8457240513909259495L;

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        if (jp.getCurrentTokenId() == JsonTokenId.ID_STRING) {
            try {

               java.util.Date date = DateUtils.parseDate(jp.getText(), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
                        "yyyy-MM-dd HH","yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSSZ","yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ss");

               return new Timestamp(date.getTime());
            }
            catch (Exception e) {
                return super.deserialize(jp, ctxt);
            }
        }
        else {
            return super.deserialize(jp, ctxt);
        }
    }
}
