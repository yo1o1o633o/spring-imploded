package com.s.imploded.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主题交换机定义,队列定义和绑定
 *
 * @author shuai.yang
 */
@Configuration
public class RabbitTopicConfiguration {

    /**
     * 创建主题交换机
     * 名字: topic.1
     * 持久化: true
     * 自动删除: false
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.1", true, false);
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue topicQueue() {
        return new Queue("s.queue.test", true);
    }

    /**
     * 绑定交换机
     */
    @Bean
    public Binding topicBinding() {
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("topic.*");
    }
}
