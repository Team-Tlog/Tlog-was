package com.se.Tlog.config.async;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.se.Tlog.global.exception.GlobalAsyncUncaughtExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
	@Autowired
	private GlobalAsyncUncaughtExceptionHandler globalAsyncUncaughtExceptionHandler;
	
	private static int CORE_POOL_SIZE = 15;
    private static int MAX_POOL_SIZE = 200;
    private static int QUEUE_CAPACITY = 2000;
    private static String THREAD_NAME_PREFIX = "async-task-pool";
    
    @Override
    public Executor getAsyncExecutor() {
    	// 작업 거부 예외(Reject Exception)는 
    	// GlobalExceptionHandler에서 처리됩니다. 
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    	// 비동기 작업 중 예외(AsyncUncaughtException)는
    	// globalAsyncUncaughtExceptionHandler에서 처리됩니다.
    	return globalAsyncUncaughtExceptionHandler;
    }
}
