package io.github.jshanet.scorpio.framework.thread;

import lombok.Getter;
import lombok.Setter;
import org.apache.tomcat.util.threads.TaskQueue;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;


/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Getter
@Setter
public class ScorpioThreadPoolTaskExecutor extends ThreadPoolTaskExecutor{

    private int queueCapacity = Integer.MAX_VALUE;

    private TaskDecorator taskDecorator;

    private boolean allowCoreThreadTimeOut = false;

    private ThreadPoolExecutor threadPoolExecutor;


    @Override
    protected ExecutorService initializeExecutor(
            ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        BlockingQueue<Runnable> queue = createQueue(queueCapacity);
        ThreadPoolExecutor executor;
        if (this.taskDecorator != null) {
            executor = new ThreadPoolExecutor(
                    this.getCorePoolSize(), this.getMaxPoolSize(), this.getKeepAliveSeconds(), TimeUnit.SECONDS,
                    queue, threadFactory, rejectedExecutionHandler) {
                @Override
                public void execute(Runnable command) {
                    super.execute(taskDecorator.decorate(command));
                }
            };
        } else {
            executor = new ThreadPoolExecutor(
                    this.getCorePoolSize(), this.getMaxPoolSize(), this.getKeepAliveSeconds(), TimeUnit.SECONDS,
                    queue, threadFactory, rejectedExecutionHandler);

        }
        if (this.allowCoreThreadTimeOut) {
            executor.allowCoreThreadTimeOut(true);
        }
        this.threadPoolExecutor = executor;
        return executor;
    }

    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new TaskQueue(queueCapacity);
        } else {
            return super.createQueue(queueCapacity);
        }
    }
}
