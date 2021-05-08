package com.s.imploded.service.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            // 当不传入ID时, 默认生成唯一, 用于消费时校验消息幂等性,如用redis判断消息重复消费
            CorrelationData correlationData = new CorrelationData();
            sRabbitTemplate.convertAndSend("s.queue.test", i, correlationData);
        }
    }

    /**
     * 写主题消息
     * */
    public void writeMessageTopic() {

    }

    /**
     * 写主题消息
     * */
    public void writeMessageDirect() {

    }

    public void writeMessageHeaders() {

    }

    public void writeMessageFanout() {

    }

    @Transactional(rollbackFor = Exception.class)
    public void writeMessageTransacted() {
        // 开启事务
        sRabbitTemplate.setChannelTransacted(true);
        sRabbitTemplate.convertAndSend("s.queue.test");
    }
}
