package com.egova.web.converter;


import com.egova.exception.FrameworkException;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;

/**
 * @Author: ldy
 * @Desctrption:
 * @Date:Create at 2018/3/29 18:18
 * @Modified By:
 */
public class StringToTimestampConverter implements Converter<String, Timestamp> {

    private final static String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd HH",
            "yyyy-MM-dd",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
    };

    @Override
    public Timestamp convert(String source) {

        try {
            java.util.Date date = DateUtils.parseDate(source, DATE_FORMATS);
            return new Timestamp(date.getTime());
        } catch (Exception ex) {
            throw new FrameworkException(String.format("Timestamp时间转换异常,转换值为%s", source));
        }
    }
}
