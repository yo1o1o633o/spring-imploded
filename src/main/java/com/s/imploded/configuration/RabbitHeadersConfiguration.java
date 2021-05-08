package com.s.imploded.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuai.yang
 */
@Configuration
public class RabbitHeadersConfiguration {
    /**
     * 创建扇形交换机
     * 名字: direct.1
     */
    @Bean
    public HeadersExchange sHeadersExchange() {
        return new HeadersExchange("direct.1");
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue sHeadersQueue() {
        return new Queue("s.queue.test", true);
    }
}
