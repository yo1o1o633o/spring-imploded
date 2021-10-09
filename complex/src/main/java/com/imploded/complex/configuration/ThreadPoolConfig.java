package com.imploded.complex.configuration;

import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author shuai.yang
 */
public class ThreadPoolConfig {

    public ThreadPoolTaskExecutor sendTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        taskExecutor.setCorePoolSize(0);
        // 设置最大线程数
        taskExecutor.setMaxPoolSize(0);
        // 设置线程活跃时间(秒)
        taskExecutor.setKeepAliveSeconds(10);
        // 设置队列容量
        taskExecutor.setQueueCapacity(20);
        // 核心线程池可以关闭
        taskExecutor.setAllowCoreThreadTimeOut(false);
        // 用于线程间传递数据
        taskExecutor.setTaskDecorator(new TaskDecorator() {
            @Override
            public Runnable decorate(Runnable runnable) {
                return null;
            }
        });
        taskExecutor.setThreadFactory(Executors.defaultThreadFactory());
        // 设置默认线程名称
        taskExecutor.setThreadNamePrefix("task-custom");
        // 设置拒绝策略, 当pool size已经达到max size的时候, 有新任务需要处理
        // AbortPolicy	        拒绝并抛出异常。
        // CallerRunsPolicy	    重试提交当前的任务，即再次调用运行该任务的execute()方法。
        // DiscardOldestPolicy	抛弃队列头部（最旧）的一个任务，并执行当前任务。
        // DiscardPolicy	    抛弃当前任务。
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 等待所有任务结束后再关闭线程池
        taskExecutor.setWaitForTasksToCompleteOnShutdown(false);
        taskExecutor.setAwaitTerminationSeconds(0);
        taskExecutor.setAwaitTerminationMillis(0L);
        taskExecutor.setBeanName("");
        taskExecutor.setThreadNamePrefix("");
        taskExecutor.setThreadPriority(0);
        taskExecutor.setDaemon(false);
        taskExecutor.setThreadGroupName("");
//        taskExecutor.setThreadGroup(new ThreadGroup());
        return taskExecutor;
    }
}
