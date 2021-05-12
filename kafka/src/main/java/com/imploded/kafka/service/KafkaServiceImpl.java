package com.imploded.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void writeMessage() {
        writeMessageTestTopics();
        writeMessageTestFilter();
    }

    /**
     * 不指定partition分区时, 根据key的hash来判断写入哪个分区
     * */
    public void writeMessageTestFilter() {
        kafkaTemplate.send("topic.2", "a", "测试消息-a");
        kafkaTemplate.send("topic.2", "b", "测试消息-b");
        kafkaTemplate.send("topic.2", "c", "测试消息-c");
        kafkaTemplate.send("topic.2", "d", "测试消息-d");
    }

    public void writeMessageTestTopics() {
        kafkaTemplate.send("topic.4", 0, "a", "测试消息-0");
        kafkaTemplate.send("topic.4", 1, "a", "测试消息-1");
        kafkaTemplate.send("topic.4", 2, "a", "测试消息-2");
        kafkaTemplate.send("topic.4", 3, "a", "测试消息-3");
        kafkaTemplate.send("topic.4", 4, "a", "测试消息-4");
        kafkaTemplate.send("topic.4", 5, "a", "测试消息-5");
        kafkaTemplate.send("topic.4", 6, "a", "测试消息-6");
        kafkaTemplate.send("topic.4", 7, "a", "测试消息-7");
        kafkaTemplate.send("topic.5", 0, "b", "测试消息--0");
        kafkaTemplate.send("topic.5", 1, "b", "测试消息--1");
    }
}
