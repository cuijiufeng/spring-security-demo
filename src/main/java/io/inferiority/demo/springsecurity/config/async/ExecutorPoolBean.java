package io.inferiority.demo.springsecurity.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @Class: PoolExecutorBean
 * @Date: 2023/4/27 13:00
 * @author: cuijiufeng
 */
@Slf4j
@EnableAsync
@Configuration
public class ExecutorPoolBean implements AsyncConfigurer {
    private final ThreadPoolTaskExecutor executor;

    public ExecutorPoolBean(@Value("${executor.core-pool-size:10}") int corePoolSize,
                            @Value("${executor.max-pool-size:100}") int maxPoolSize,
                            @Value("${executor.keep-alive-seconds:60}") int keepAliveSeconds,
                            @Value("${executor.queue-capacity:1000}") int queueCapacity) {
        executor = new ThreadPoolTaskExecutor();
        //设置线程名字，好区分
        executor.setThreadFactory(new CustomizableThreadFactory("thread-executors-"));
        //核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 缓存队列
        executor.setQueueCapacity(queueCapacity);
        //线程空闲后最大存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //设置拒绝策略，异步改同步
        executor.setRejectedExecutionHandler((runnable, pool) -> {
            if (!pool.isShutdown()) {
                log.info("线程池阻塞列队超出，改同步执行");
                runnable.run();
                return;
            }
            log.info("线程池已被关闭，任务丢弃");
        });
        executor.initialize();
    }

    @NonNull
    @Override
    public Executor getAsyncExecutor() {
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            if (log.isErrorEnabled()) {
                String paramsStr = Arrays.stream(params)
                        .map(param -> param.getClass().getSimpleName())
                        .collect(Collectors.joining(", ", "(", ")"));
                log.error(String.format("thread pool async execute method %s%s%nerror: ", method.getName(), paramsStr), ex);
            }
        };
    }
}
