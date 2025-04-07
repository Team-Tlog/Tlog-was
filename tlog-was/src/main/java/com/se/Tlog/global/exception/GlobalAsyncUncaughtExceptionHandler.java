package com.se.Tlog.global.exception;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
public class GlobalAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("Error occurred - Async uncaught exception : [errorCode={}, message={}]", ex.getClass(), ex.getMessage(), ex);
	}
}
