package com.imploded.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "topic.1")
    public void receive(String msg) {
        log.info("收到消息: {}", msg);
    }

    @KafkaListener(topics = "topic.2", containerFactory = "containerFactory")
    public void receiveFilter(ConsumerRecord<?, ?> record) {
        log.info("收到消息: key:{}, value:{}", record.key(), record.value());
    }

    /**
     * topicPartitions支持监听多个主题的多个分区
     * 在设置此注解时不能同时配置topics
     * */
    @KafkaListener(id = "4", topicPartitions = {
            @TopicPartition(topic = "topic.4", partitions = {"2", "3"}),
            @TopicPartition(topic = "topic.5", partitions = "0")
    })
    public void receiveP(ConsumerRecord<?, ?> record) {
        log.info("收到消息: key:{}, value:{}", record.key(), record.value());
    }
}
