package com.imploded.kafka.controller;

import com.imploded.kafka.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuai.yang
 */
@RestController
public class KafkaController {
    @Autowired
    KafkaService kafkaService;

    @RequestMapping("/kafka/write/message")
    public void writeMessage() {
        kafkaService.writeMessage();
    }
}
