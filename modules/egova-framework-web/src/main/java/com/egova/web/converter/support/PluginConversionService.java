package com.egova.web.converter.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.GenericConversionService;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 
* @Description: 全局类型转换注册入口
* @author chendb
* @date 2015年11月10日 下午2:50:55
*
 */
public class PluginConversionService implements ConversionService {

	@Autowired
	private GenericConversionService conversionService;
	private Set<?> converters;

	@PostConstruct
	public void afterPropertiesSet() {
		if (converters != null) {
			for (Object converter : converters) {
				if (converter instanceof Converter<?, ?>) {
					conversionService.addConverter((Converter<?, ?>) converter);
				} else if (converter instanceof ConverterFactory<?, ?>) {
					conversionService.addConverterFactory((ConverterFactory<?, ?>) converter);
				} else if (converter instanceof GenericConverter) {
					conversionService.addConverter((GenericConverter) converter);
				}
			}
		}
	}

	@Override
	public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
		return conversionService.canConvert(sourceType, targetType);
	}

	@Override
	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return conversionService.canConvert(sourceType, targetType);
	}

	@Override
	public <T> T convert(Object source, Class<T> targetType) {
		return conversionService.convert(source, targetType);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return conversionService.convert(source, sourceType, targetType);
	}

	public Set<?> getConverters() {
		return converters;
	}

	public void setConverters(Set<?> converters) {
		this.converters = converters;
	}

}
