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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
}
