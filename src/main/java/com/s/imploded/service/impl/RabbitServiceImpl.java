package com.s.imploded.service.impl;

import com.s.imploded.service.RabbitCallbackService;
import com.s.imploded.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class RabbitServiceImpl implements RabbitService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitTemplate ackRabbitTemplate;

    @Override
    public void writeMessage() {
        // 开启事务
        ackRabbitTemplate.setChannelTransacted(true);
        // 消息回调
        ackRabbitTemplate.setReturnsCallback(new RabbitCallbackService());

        rabbitTemplate.convertAndSend("y.queue.test", "测试消息");
    }
}
