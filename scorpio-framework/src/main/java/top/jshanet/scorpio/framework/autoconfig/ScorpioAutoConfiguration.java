package top.jshanet.scorpio.framework.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import top.jshanet.scorpio.framework.core.service.ScorpioRequestNoService;
import top.jshanet.scorpio.framework.service.RequestNoService;

/**
 * @author seanjiang
 * @since 2020-07-14
 */
@Configuration
@EnableConfigurationProperties(ScorpioWebMvcAsyncProperties.class)
@EnableJpaAuditing
@EnableTransactionManagement
public class ScorpioAutoConfiguration {

    @Autowired
    private ScorpioWebMvcAsyncProperties scorpioWebMvcAsyncProperties;


    @Bean("webTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        ScorpioWebMvcAsyncProperties.ThreadPoolProperties threadPoolProperties = scorpioWebMvcAsyncProperties.getThreadPool();
        threadPoolTaskExecutor.setCorePoolSize(threadPoolProperties.getCoolPoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        threadPoolTaskExecutor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        threadPoolTaskExecutor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(threadPoolProperties.isAllowCoreThreadTimeOut());
        threadPoolTaskExecutor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.isWaitForTasksToCompleteOnShutdown());
        threadPoolTaskExecutor.setThreadNamePrefix(threadPoolProperties.getThreadNamePrefix());
        return threadPoolTaskExecutor;
    }

    @Bean
    @ConditionalOnMissingBean(RequestNoService.class)
    public RequestNoService requestNoService() {
        return new ScorpioRequestNoService();
    }



}
