package com.s.imploded.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

/**
 * @author shuai.yang
 */
@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "topic.1")
    public void receive(String msg, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
    }
}
