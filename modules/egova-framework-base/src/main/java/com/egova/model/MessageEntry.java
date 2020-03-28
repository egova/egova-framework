package com.egova.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 异常对象
 * 
 * @author chendb
 * @date 2016年12月8日 下午11:41:29
 */
@Data
@AllArgsConstructor
public class MessageEntry {

    private String name;
    private String message;
    private Boolean success;

    public static MessageEntry error(String name, String message){
        return new MessageEntry(name,message,false);
    }
    public static MessageEntry success(String name, String message){
        return new MessageEntry(name,message,true);
    }
    public static MessageEntry message(String name, String message){
        return new MessageEntry(name,message,null);
    }
}
