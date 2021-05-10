package com.imploded.rabbit.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 扇形交换机-广播
 * @author shuai.yang
 */
@Configuration
public class RabbitFanoutConfiguration {
    /**
     * 创建扇形交换机
     * 名字: s.exchange.fanout
     */
    @Bean
    public FanoutExchange sFanoutExchange() {
        return new FanoutExchange("s.exchange.fanout");
    }

    /**
     * 创建队列
     * 持久化: true
     */
    @Bean
    public Queue sFanoutQueue1() {
        return new Queue("s.queue.fanout.1", true);
    }

    /**
     * 创建队列
     * 持久化: true
     */
    @Bean
    public Queue sFanoutQueue2() {
        return new Queue("s.queue.fanout.2", true);
    }

    /**
     * 创建队列
     * 持久化: true
     */
    @Bean
    public Queue sFanoutQueue3() {
        return new Queue("s.queue.fanout.3", true);
    }

    /**
     * 所有绑定了该交换机的队列都会收到消息
     * 绑定交换机
     */
    @Bean
    public Binding sFanoutBinding1() {
        return BindingBuilder.bind(sFanoutQueue1()).to(sFanoutExchange());
    }
    @Bean
    public Binding sFanoutBinding2() {
        return BindingBuilder.bind(sFanoutQueue2()).to(sFanoutExchange());
    }
    @Bean
    public Binding sFanoutBinding3() {
        return BindingBuilder.bind(sFanoutQueue3()).to(sFanoutExchange());
    }
}
