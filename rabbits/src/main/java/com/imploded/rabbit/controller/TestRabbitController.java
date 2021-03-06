package com.imploded.rabbit.controller;

import com.imploded.rabbit.service.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuai.yang
 */
@RestController
public class TestRabbitController {
    @Autowired
    RabbitService rabbitService;

    @RequestMapping("/rabbit/write/message")
    public void writeMessage() {
        rabbitService.writeMessageDirect();
        rabbitService.writeMessageFanout();
        rabbitService.writeMessageHeaders();
        rabbitService.writeMessageTopic();
    }
}
