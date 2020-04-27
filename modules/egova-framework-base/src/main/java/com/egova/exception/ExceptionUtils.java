package com.egova.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ExceptionUtils
{
	private static final String NULL = null;

	/**
	 * 获得完整消息，包括异常名
	 *
	 * @param e 异常
	 * @return 完整消息
	 */
	public static String getMessage(Throwable e) {
		if (null == e) {
			return NULL;
		}
		return String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage());
	}

	/**
	 * 获得消息，调用异常类的getMessage方法
	 *
	 * @param e 异常
	 * @return 消息
	 */
	public static String getSimpleMessage(Throwable e) {
		return (null == e) ? NULL : e.getMessage();
	}

	/**
	 * 使用运行时异常包装编译异常
	 *
	 * @param throwable 异常
	 * @return 运行时异常
	 */
	public static RuntimeException wrapRuntime(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			return (RuntimeException) throwable;
		}
		if (throwable instanceof Error) {
			throw (Error) throwable;
		}
		return new RuntimeException(throwable);
	}

	/**
	 * 剥离反射引发的InvocationTargetException、UndeclaredThrowableException中间异常，返回业务本身的异常
	 *
	 * @param wrapped 包装的异常
	 * @return 剥离后的异常
	 */
	public static Throwable unwrap(Throwable wrapped) {
		Throwable unwrapped = wrapped;
		while (true) {
			if (unwrapped instanceof InvocationTargetException) {
				unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
			} else if (unwrapped instanceof UndeclaredThrowableException) {
				unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
			} else {
				return unwrapped;
			}
		}
	}

	/**
	 * 获取当前栈信息
	 *
	 * @return 当前栈信息
	 */
	public static StackTraceElement[] getStackElements() {
		// return (new Throwable()).getStackTrace();
		return Thread.currentThread().getStackTrace();
	}

	/**
	 * 获取指定层的堆栈信息
	 *
	 * @return 指定层的堆栈信息
	 * @since 4.1.4
	 */
	public static StackTraceElement getStackElement(int i) {
		return getStackElements()[i];
	}

	/**
	 * 获取入口堆栈信息
	 *
	 * @return 入口堆栈信息
	 * @since 4.1.4
	 */
	public static StackTraceElement getRootStackElement() {
		final StackTraceElement[] stackElements = getStackElements();
		return stackElements[stackElements.length - 1];
	}

	/**
	 * 判断是否由指定异常类引起
	 *
	 * @param throwable 异常
	 * @param causeClasses 定义的引起异常的类
	 * @return 是否由指定异常类引起
	 * @since 4.1.13
	 */
	public static boolean isCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
		return null != getCausedBy(throwable, causeClasses);
	}

	/**
	 * 获取由指定异常类引起的异常
	 *
	 * @param throwable 异常
	 * @param causeClasses 定义的引起异常的类
	 * @return 是否由指定异常类引起
	 * @since 4.1.13
	 */
	public static Throwable getCausedBy(Throwable throwable, Class<? extends Exception>... causeClasses) {
		Throwable cause = throwable;
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeClasses) {
				if (causeClass.isInstance(cause)) {
					return cause;
				}
			}
			cause = cause.getCause();
		}
		return null;
	}


	/**
	 * 基础架构异常
	 *
	 * @param message   消息
	 * @param throwable 异常
	 * @return
	 */
	public static FrameworkException framework(String message, Throwable throwable) {
		return new FrameworkException(message, throwable);
	}

	/**
	 * 基础架构异常
	 *
	 * @param throwable 异常
	 * @param format    消息format
	 * @param args      消息参数
	 * @return
	 */
	public static FrameworkException framework(Throwable throwable, String format, Object... args) {

		return new FrameworkException(String.format(format, args), throwable);
	}

	/**
	 * 基础架构异常
	 *
	 * @param format 消息format
	 * @param args   消息参数
	 * @return
	 */
	public static FrameworkException framework(String format, Object... args) {

		return new FrameworkException(String.format(format, args));
	}

	public static RuntimeException runtime(Throwable throwable) {
		if (throwable instanceof RuntimeException) {
			return (RuntimeException) throwable;
		}
		if (throwable instanceof Error) {
			throw (Error) throwable;
		}
		return new RuntimeException(throwable);
	}


	public static RuntimeException runtime(String message, Throwable throwable) {
		return new RuntimeException(message, throwable);
	}

	public static RuntimeException runtime(String format, Object... args) {
		return new RuntimeException(String.format(format, args));
	}

	public static RuntimeException runtime(Throwable throwable, String format, Object... args) {

		return new RuntimeException(String.format(format, args), throwable);
	}

	public static NullPointerException nullPointer(String format, Object... args) {
		return new NullPointerException(String.format(format, args));
	}


	public static IllegalArgumentException argument(String format, Object... args) {
		return new IllegalArgumentException(String.format(format, args));
	}

	public static IllegalArgumentException argument(Throwable throwable, String format, Object... args) {
		return new IllegalArgumentException(String.format(format, args), throwable);
	}

	public static IllegalArgumentException argument(String message, Throwable throwable) {
		return new IllegalArgumentException(message, throwable);
	}

	public static UnsupportedOperationException unsupported(Throwable throwable, String format, Object... args) {

		return new UnsupportedOperationException(String.format(format, args), throwable);
	}

	public static UnsupportedOperationException unsupported(String format, Object... args) {
		return new UnsupportedOperationException(String.format(format, args));
	}

	public static UnsupportedOperationException unsupported(String message, Throwable throwable) {
		return new UnsupportedOperationException(message, throwable);
	}


	public static ApiException api(Throwable throwable, String format, Object... args) {

		return new ApiException(0L, String.format(format, args), throwable);
	}

	public static ApiException api(String format, Object... args) {
		return new ApiException(0L, String.format(format, args));
	}

	public static ApiException api(String message, Throwable throwable) {
		return new ApiException(0L, message, throwable);
	}
}
