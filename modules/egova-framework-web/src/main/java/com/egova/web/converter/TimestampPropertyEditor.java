package com.egova.web.converter;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class TimestampPropertyEditor extends PropertyEditorSupport {
	 
    private String pattern = "yyyy-MM-dd HH:mm:ss.S";
 
    private String[] formater_pattern = new String[] { "yyyy-MM-dd HH:mm:ss.S",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
 
    /**
     * 
     */
    public TimestampPropertyEditor() {
        // TODO Auto-generated constructor stub
    }
 
    public TimestampPropertyEditor(String pattern) {
        // TODO Auto-generated constructor stub
        this.pattern = pattern;
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            try {
                Date date = DateUtils.parseDate(text, formater_pattern);
                Timestamp timestamp = new Timestamp(date.getTime());
                //              设置转换完的值
                setValue(timestamp);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                setValue(null);
            }
        }
 
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyEditorSupport#getAsText()
     */
    @Override
    public String getAsText() {
        // TODO Auto-generated method stub
        //  获取model的值
        Timestamp value = (Timestamp) getValue();
        if (value == null) {
            return "";
        } else {
            try {
                Date date = new Date(value.getTime());
                String str = DateFormatUtils.format(date, pattern);
                return str;
            } catch (Exception e) {
                return "";
            }
        }
 
    }
 
}

