package com.egova.web.converter;


import org.springframework.core.convert.converter.Converter;

public class StringToBooleanConverter implements Converter<String, Boolean>{

    @Override
    public Boolean convert(String value) {

        if (value.equals("0")) {
            return Boolean.FALSE;
        }
        if (value.equals("1")) {
            return Boolean.TRUE;
        }
        if (value.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (value.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(value);
    }

}
