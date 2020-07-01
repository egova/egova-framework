package com.egova.json.databind.std;

import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义时间格式化器
 * 
 * @author chendb
 * @date 2016年12月8日 下午11:36:09
 */
public class CustomDateFormat extends SimpleDateFormat {

    /**
     * 
     */
    private static final long serialVersionUID = 9211535820162009046L;

    // 默认序列化格式，否则使用user.language.format
    public CustomDateFormat() {
        super("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    @Override
    public Date parse(String source) throws ParseException {
        try {
            return DateUtils.parseDate(source, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM-dd",
                    "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss.SSSZ","yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ss");
        }
        catch (Exception ex) {
            return super.parse(source);
        }
    }
}
