package com.egova.web.converter;


import org.springframework.core.convert.converter.Converter;

public class StringToBooleanConverter implements Converter<String, Boolean>{

    @Override
    public Boolean convert(String value) {

        if ("0".equals(value)) {
            return Boolean.FALSE;
        }
        if ("1".equals(value)) {
            return Boolean.TRUE;
        }
        if ("true".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(value);
    }

}
