package com.imploded.kafka.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

/**
 * @author shuai.yang
 */
@Configuration
public class KafkaConfiguration {
    @Autowired
    ConsumerFactory<Object, Object> consumerFactory;

    /**
     * 创建主题: 8个分区, 2个副本
     * 修改分区不会导致数据丢失, 但是分区数只能增大不能减少
     * */
    @Bean
    public NewTopic topic1() {
        return new NewTopic("topic.4", 8, (short) 1);
    }
    @Bean
    public NewTopic topic2() {
        return new NewTopic("topic.5", 2, (short) 1);
    }

    /**
     * 消息过滤器
     * */
    @Bean
    public ConcurrentKafkaListenerContainerFactory containerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory);
        // 被过滤的消息被丢弃
        containerFactory.setAckDiscarded(true);
        // 消息过滤策略, 返回true消息被过滤
        containerFactory.setRecordFilterStrategy(consumerRecord -> {
            String key = (String) consumerRecord.key();
            return "a".equals(key);
        });
        return containerFactory;
    }
}
