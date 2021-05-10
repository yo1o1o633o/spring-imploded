package com.imploded.rabbit.configuration;

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
        return new TopicExchange("s.exchange.topic", true, false);
    }

    /**
     * 创建队列
     * 名字: s.queue.test
     * 持久化: true
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue("s.queue.topic.1", true);
    }
    @Bean
    public Queue topicQueue2() {
        return new Queue("s.queue.topic.2", true);
    }

    /**
     * 绑定交换机
     */
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.1");
    }
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }
}
