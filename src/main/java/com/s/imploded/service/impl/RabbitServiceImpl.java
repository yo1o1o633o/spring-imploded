package com.s.imploded.service.impl;

import com.s.imploded.service.RabbitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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
    RabbitTemplate sRabbitTemplate;

    @Override
    public void writeMessage() {
        for (int i = 0; i < 100; i++) {
            // 当不传入ID时, 默认生成唯一
            CorrelationData correlationData = new CorrelationData();
            sRabbitTemplate.convertAndSend("s.queue.test", i, correlationData);
        }
    }

    public void writeMessageTransacted() {
        // 开启事务
        sRabbitTemplate.setChannelTransacted(true);
    }
}
