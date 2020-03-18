package io.github.jshanet.scorpio.framework.configuration;

import io.github.jshanet.scorpio.framework.common.thread.ScorpioThreadPoolTaskExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author seanjiang
 * @date 2019/12/26
 */
@Configuration
public class ScorpioWebMvcConfiguration implements WebMvcConfigurer {

    @Bean(name = "frontTaskExecutor")
    public ThreadPoolTaskExecutor frontThreadPoolTaskExecutor(
            @Value("${sapphire.front.threadpool.manager-pool-size:48}") int frontCorePoolSize,
            @Value("${sapphire.front.threadpool.keep-alive-seconds:60}") int frontKeepAliveSeconds,
            @Value("${sapphire.front.threadpool.max-pool-size:48}") int frontMaxPoolSize,
            @Value("${sapphire.front.threadpool.queue-capacity:300}") int frontQueueCapacity,
            @Value("${sapphire.front.threadpool.allow-manager-thread-timeout:true}") boolean frontAllowCoreThreadTimeOut,
            @Value("${sapphire.front.threadpool.await-termination-seconds:60}") int frontAwaitTerminationSeconds,
            @Value("${sapphire.front.threadpool.wait-for-task-to-complete-on-shutdown:true}") boolean waitForTasksToCompleteOnShutdown) {
        ThreadPoolTaskExecutor executor = new ScorpioThreadPoolTaskExecutor();
        executor.setCorePoolSize(frontCorePoolSize);
        executor.setKeepAliveSeconds(frontKeepAliveSeconds);
        executor.setMaxPoolSize(frontMaxPoolSize);
        executor.setQueueCapacity(frontQueueCapacity);
        executor.setAllowCoreThreadTimeOut(frontAllowCoreThreadTimeOut);
        executor.setAwaitTerminationSeconds(frontAwaitTerminationSeconds);
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

}
