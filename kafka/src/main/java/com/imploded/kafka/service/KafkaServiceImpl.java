package com.imploded.kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        kafkaTemplate.send("topic.1", "测试消息-1");
        kafkaTemplate.send("topic.1", "a", "测试消息-a");
        kafkaTemplate.send("topic.1", 0, "a", "测试消息-a");
        kafkaTemplate.send("topic.1", 0, 1L, "a", "测试消息-a");


        kafkaTemplate.send("topic.1", "测试消息-callback").addCallback(new KafkaCallback());
    }
}
