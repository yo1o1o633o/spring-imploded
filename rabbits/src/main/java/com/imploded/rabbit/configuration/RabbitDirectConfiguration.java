package com.imploded.rabbit.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直连交换机
 *
 * @author shuai.yang
 */
@Configuration
public class RabbitDirectConfiguration {
    /**
     * 创建直连交换机
     * 名字: direct.1
     */
    @Bean
    public DirectExchange sDirectExchange() {
        return new DirectExchange("s.exchange.direct.1");
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sDirectQueue1() {
        return new Queue("s.queue.direct.1", true);
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sDirectQueue2() {
        return new Queue("s.queue.direct.2", true);
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sDirectQueue3() {
        return new Queue("s.queue.direct.3", true);
    }

    /**
     * 绑定交换机, 一个交换机绑定3个队列. 同时绑定2种路由key, 消息根据路由key分发
     * 路由key: "s.direct.route.1" 绑定在1和2上, 当写入消息指定key为s.direct.route.1时, 队列1和队列2同时收到消息
     */
    @Bean
    public Binding sDirectBinding1() {
        return BindingBuilder.bind(sDirectQueue1()).to(sDirectExchange()).with("s.direct.route.1");
    }
    @Bean
    public Binding sDirectBinding2() {
        return BindingBuilder.bind(sDirectQueue2()).to(sDirectExchange()).with("s.direct.route.1");
    }
    @Bean
    public Binding sDirectBinding3() {
        return BindingBuilder.bind(sDirectQueue3()).to(sDirectExchange()).with("s.direct.route.2");
    }
}
