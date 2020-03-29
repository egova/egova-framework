package com.egova.web.converter;


import com.flagwind.lang.CodeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.EnumSet;


public class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

	@Override
	public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
		return new StringToEnum(targetType);
	}

	private class StringToEnum<T extends Enum> implements Converter<String, T> {

		private final Class<T> enumType;

		public StringToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		@Override
		public T convert(String source) {
			if (source.length() == 0) {
				return null;
			}

			EnumSet currEnumSet = EnumSet.allOf(enumType);

			for (Object element : currEnumSet) {

				T t = (T) element;

				if (t instanceof CodeType) {
					CodeType descriptor = (CodeType) t;
					if (descriptor.getValue().equals(source)) {
						return t;
					}
				}

				if (t.name().equals(source) || String.valueOf(t.ordinal()).equals(source)) {
					return t;
				}
			}
			return null;
		}
	}

}