package com.s.imploded.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuai.yang
 */
@Configuration
public class RabbitFanoutConfiguration {
    /**
     * 创建扇形交换机
     * 名字: direct.1
     */
    @Bean
    public FanoutExchange sFanoutExchange() {
        return new FanoutExchange("s.exchange.fanout");
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sFanoutQueueA() {
        return new Queue("s.queue.fanout.a", true);
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sFanoutQueueB() {
        return new Queue("s.queue.fanout.b", true);
    }

    /**
     * 绑定交换机
     */
    @Bean
    public Binding sFanoutBindingA() {
        return BindingBuilder.bind(sFanoutQueueA()).to(sFanoutExchange());
    }

    /**
     * 绑定交换机
     */
    @Bean
    public Binding sFanoutBindingB() {
        return BindingBuilder.bind(sFanoutQueueB()).to(sFanoutExchange());
    }
}
