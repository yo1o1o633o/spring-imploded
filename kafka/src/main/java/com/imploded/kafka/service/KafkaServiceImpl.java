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
    KafkaTemplate<String, String> kafkaTemplate;

    public void write() {
        kafkaTemplate.send("topic.1", "测试消息-1");
    }
}
