package com.imploded.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
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
}
