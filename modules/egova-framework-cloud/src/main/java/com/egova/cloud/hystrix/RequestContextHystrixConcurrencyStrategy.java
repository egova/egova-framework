package com.egova.cloud.hystrix;

import com.egova.cloud.feign.RequestUtils;
import com.egova.cloud.feign.TokenHolder;
import com.egova.cloud.web.UserContextHolder;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 当feign开启hystrix，并采用线程隔离时，提供请求上下文传递
 * <p>
 * https://www.cnblogs.com/duanxz/p/10949816.html
 *
 * @author 奔波儿灞
 * @since 1.0
 */
public class RequestContextHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    RequestContextHystrixConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties threadPoolProperties) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getThreadPool(threadPoolKey, threadPoolProperties)
                : super.getThreadPool(threadPoolKey, threadPoolProperties);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy
                .wrapCallable(new RequestAttributeAwareCallable<>(callable, RequestContextHolder.getRequestAttributes(), RequestUtils.getToken()))
                : super.wrapCallable(new RequestAttributeAwareCallable<>(callable, RequestContextHolder.getRequestAttributes(), RequestUtils.getToken()));
    }

    static class RequestAttributeAwareCallable<T> implements Callable<T> {

        private final Callable<T> delegate;
        private final RequestAttributes requestAttributes;
        private final String token;

        RequestAttributeAwareCallable(Callable<T> callable, RequestAttributes requestAttributes, String token) {
            this.delegate = callable;
            this.requestAttributes = requestAttributes;
            this.token = token;
        }

        @Override
        public T call() throws Exception {
            try {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                // 防止异步线程执行任务时，而http线程已经清理request导致feign丢失token
                TokenHolder.setup(token);
                // 防止异步线程执行任务时，而http线程已经清理request导致后续丢失用户信息
                UserContextHolder.setup();
                return delegate.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
                TokenHolder.cleanup();
                UserContextHolder.cleanup();
            }
        }
    }
}
