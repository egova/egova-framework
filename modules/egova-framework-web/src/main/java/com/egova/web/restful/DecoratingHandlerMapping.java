package com.egova.web.restful;

import com.egova.web.annotation.RequestDecorating;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import strman.Strman;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;


public class DecoratingHandlerMapping extends RequestMappingHandlerMapping {

//	private static final Log logger = LogFactory.getLog(DecoratingHandlerMapping.class);

	private final static Map<String, HandlerMethod> HANDLER_METHOD_MAP = new HashMap<>();

//	private final static String HANDLER_METHOD_KEY_PATTERN = "%s[%s]@%s";


	private List<HandlerMethodInterceptor> handlerMethodInterceptors;

	public DecoratingHandlerMapping(List<HandlerMethodInterceptor> handlerMethodInterceptors) {
		this.handlerMethodInterceptors = handlerMethodInterceptors;
	}

	@Override
	protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
//		RequestDecorating state = method.getAnnotation(RequestDecorating.class);
//		if (state != null) {
//			registerStateHandlerMethod(handler, method, mapping, state);
//			super.registerHandlerMethod(handler, method, mapping);
//			return;
//		}
		super.registerHandlerMethod(handler, method, mapping);

		if (handlerMethodInterceptors != null) {
			for (HandlerMethodInterceptor interceptor : handlerMethodInterceptors) {
				if (interceptor.preHandle(handler, method, mapping)) {
					interceptor.postHandle(handler, method, mapping);
				}
			}
		}

	}

	@Override
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
		//String decoratingKey = this.tryResolveDecoratingKey(request);
		//if (StringUtils.hasLength(decoratingKey)) {
//		HandlerMethod restApiHandlerMethod = lookupStateHandlerMethod(lookupPath, request);
//		if (restApiHandlerMethod != null)
//			return restApiHandlerMethod;
		//}
		return super.lookupHandlerMethod(lookupPath, request);
	}

//
//	private void registerStateHandlerMethod(Object handler, Method method, RequestMappingInfo mapping, RequestDecorating decorating) {
//
//		PatternsRequestCondition patternsCondition = mapping.getPatternsCondition();
//		if (patternsCondition.getPatterns().size() == 0) {
//			return;
//		}
//		List<String> requestKeys = getRequestKeys(mapping, decorating);
//		HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);
//		for (String key : requestKeys) {
//			if (!HANDLER_METHOD_MAP.containsKey(key)) {
//				HANDLER_METHOD_MAP.put(key, handlerMethod);
//				if (logger.isDebugEnabled()) {
//					logger.debug(String.format("register state HandlerMethod of %s %s", key, handlerMethod));
//				}
//			}
//		}
//
//	}

//	private List<String> getDecoratingKeys(RequestDecorating decorating) {
//		List<String> requestKeys = new ArrayList<>();
//		String[] states = decorating.state();
//		String[] versions = decorating.version();
//		if (states.length == 0) {
//			for (String v : versions) {
//				requestKeys.add(":" + v);
//			}
//		}
//		for (String state : states) {
//			if (versions.length == 0) {
//				requestKeys.add(Strman.toKebabCase(state) + ":");
//				continue;
//			}
//			for (String v : versions) {
//				requestKeys.add(Strman.toKebabCase(state) + ":" + v);
//			}
//		}
//		return requestKeys;
//	}

//	private List<String> getRequestKeys(RequestMappingInfo mapping, RequestDecorating decorating) {
//		PatternsRequestCondition patternsCondition = mapping.getPatternsCondition();
//		RequestMethodsRequestCondition methodsCondition = mapping.getMethodsCondition();
//
//		Iterator<String> patternIterator = patternsCondition.getPatterns().iterator();
//		RequestMethod[] requestMethods = methodsCondition.getMethods().toArray(new RequestMethod[methodsCondition.getMethods().size()]);
//		List<String> requestKeys = new ArrayList<>();
//
//		while (patternIterator.hasNext()) {
//			String patternItem = patternIterator.next();
//			if (requestMethods.length == 0) {
//				for (String decoratingKey : getDecoratingKeys(decorating)) {
//					String key = String.format(HANDLER_METHOD_KEY_PATTERN, patternItem, "", decoratingKey);
//					requestKeys.add(key);
//				}
//				continue;
//			}
//
//			for (RequestMethod methodItem : requestMethods) {
//				for (String decoratingKey : getDecoratingKeys(decorating)) {
//					String key = String.format(HANDLER_METHOD_KEY_PATTERN, patternItem, methodItem.name(), decoratingKey);
//					requestKeys.add(key);
//				}
//
//			}
//		}
//		return requestKeys;
//	}
//
//	private HandlerMethod lookupStateHandlerMethod(String lookupPath, HttpServletRequest request) {
//		String decoratingKey = tryResolveDecoratingKey(request);
//		if (StringUtils.hasText(decoratingKey)) {
//			String key = String.format(HANDLER_METHOD_KEY_PATTERN, lookupPath, request.getMethod(), decoratingKey);
//			HandlerMethod handlerMethod = HANDLER_METHOD_MAP.get(key);
//			if (handlerMethod == null) {
//				key = String.format(HANDLER_METHOD_KEY_PATTERN, lookupPath, "", decoratingKey);
//				handlerMethod = HANDLER_METHOD_MAP.get(key);
//			}
//			if (handlerMethod != null) {
//				if (logger.isDebugEnabled()) {
//					logger.debug(String.format("lookup decorating HandlerMethod of %s %s", key, handlerMethod));
//				}
//				return handlerMethod;
//			}
//			logger.debug(String.format("lookup decorating HandlerMethod of %s failed", key));
//		}
//		return null;
//	}

//	private String tryResolveDecoratingKey(HttpServletRequest request) {
//		String state = request.getParameter("@state");
//		String version = request.getParameter("@version");
//		if (state == null) state = "";
//		if (version == null) version = "";
//		return Strman.toKebabCase(state) + ":" + version;
//	}


	@Override
	protected RequestCondition<DecoratingRequestCondition> getCustomMethodCondition(Method method) {
		RequestDecorating sate = AnnotationUtils.findAnnotation(method, RequestDecorating.class);
		return createCondition(sate);
	}

	@Override
	protected RequestCondition<DecoratingRequestCondition> getCustomTypeCondition(Class<?> handlerType) {
		RequestDecorating state = AnnotationUtils.findAnnotation(handlerType, RequestDecorating.class);
		return createCondition(state);
	}

	private RequestCondition<DecoratingRequestCondition> createCondition(RequestDecorating decorating) {
		return decorating == null ? null : new DecoratingRequestCondition(decorating.state(), decorating.version());
	}

}
