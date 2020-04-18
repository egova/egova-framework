package com.egova.application;

import com.flagwind.application.ContentBase;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 应用程序上下文
 * @author chendb
 * @date 2020-04-17 14:17:06
 */
public class Context implements ContentBase {

    private org.springframework.context.ApplicationContext context;

    public Context(org.springframework.context.ApplicationContext context) {
        this.context = context;
    }

    @Override
    public <T> T resolve(String name) {
        return (T) context.getBean(name);
    }

    @Override
    public <T> T resolve(Class<T> serviceType) {
        return context.getBean(serviceType);
    }

    @Override
    public <T> T resolve(Class<T> serviceType, String name) {
        return context.getBean(name, serviceType);
    }

    @Override
    public <T> List<T> resolveAll(Class<T> serviceType) {
        Map<String, T> map = context.getBeansOfType(serviceType);
        List<T> list = new ArrayList<>(map.values());
        AnnotationAwareOrderComparator.sort(list);
        return list;
    }
}
