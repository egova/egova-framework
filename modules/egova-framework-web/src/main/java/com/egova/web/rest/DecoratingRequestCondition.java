package com.egova.web.rest;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import strman.Strman;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


public class DecoratingRequestCondition implements RequestCondition<DecoratingRequestCondition> {

    private String[] states;



    public DecoratingRequestCondition(String[] states) {
        this.states = Arrays.stream(states).map(Strman::toKebabCase).toArray(String[]::new);
    }

    // 这里combine()方法主要是供给复合类型的RequestMapping使用的，这种类型的Mapping可以持有
    // 两个Mapping信息，因而需要对两个Mapping进行合并，这个合并的过程其实就是对每个RequestMappingInfo
    // 中的各个条件进行合并，这里就是对RequestCondition条件进行合并
    @NotNull
    public DecoratingRequestCondition combine(DecoratingRequestCondition other) {
        String[] allStates = merge(states, other.states);
//        String[] allVersions = merge(versions, other.versions);
        return new DecoratingRequestCondition(allStates);
    }

    // 判断当前请求对应用户选择的模板与当前接口所能处理的模板是否一致，
    // 如果一致则返回当前RequestCondition，这里RequestMappingHandlerMapping在匹配请求时，
    // 如果当前条件的匹配结果不为空，则说明当前条件是能够匹配上的，如果返回值为空，则说明其不能匹配
    public DecoratingRequestCondition getMatchingCondition(HttpServletRequest request) {
        String s = Strman.toKebabCase(request.getParameter("@state"));
        String v = request.getParameter("@version");
        if (StringUtils.isEmpty(s) && StringUtils.isEmpty(v)) {
            return null;
        }
        if (StringUtils.isNoneEmpty(s) && Arrays.stream(states).noneMatch(g -> g.equalsIgnoreCase(s))) {
            return null;
        }


        return this;
    }


    // 对两个RequestCondition对象进行比较，这里主要是如果存在两个注册的一样的Mapping，那么就会对
    // 这两个Mapping进行排序，以判断哪个Mapping更适合处理当前request请求
    public int compareTo(@NotNull DecoratingRequestCondition other, @NotNull HttpServletRequest request) {
        return null != states && null == other.states ? 1
                : null == states && null != other.states ? -1 : 0;
    }


    private String[] merge(String[] sources, String[] otherTemplates) {
        if (null == otherTemplates) {
            return sources;
        }
        otherTemplates = Arrays.stream(otherTemplates).map(Strman::toKebabCase).toArray(String[]::new);
        String[] results = new String[sources.length + otherTemplates.length];
        System.arraycopy(sources, 0, results, 0, sources.length);
        System.arraycopy(otherTemplates, 0, results, sources.length, results.length - sources.length);
        return results;
    }
}
