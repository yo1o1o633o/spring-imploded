package com.imploded.complex.service.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * @author shuai.yang
 */
public interface MessageService {
    ProducerRecord<String, String> makeMessage();

    void receiveMessage();
}
