package com.s.imploded.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 完全匹配
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
        return new DirectExchange("direct.1");
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sDirectQueue() {
        return new Queue("s.queue.direct", true);
    }

    /**
     * 绑定交换机
     */
    @Bean
    public Binding sDirectBinding() {
        return BindingBuilder.bind(sDirectQueue()).to(sDirectExchange()).with("s.queue.direct");
    }
}
