package com.s.imploded.service.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
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
        writeMessageHeaders();
//        for (int i = 0; i < 100; i++) {
//            // 当不传入ID时, 默认生成唯一, 用于消费时校验消息幂等性,如用redis判断消息重复消费
//            CorrelationData correlationData = new CorrelationData();
//            sRabbitTemplate.convertAndSend("s.queue.test", i, correlationData);
//        }
    }

    /**
     * 写主题消息
     * */
    public void writeMessageTopic() {

    }

    /**
     * 写主题消息
     * */
    @Override
    public void writeMessageDirect() {
        sRabbitTemplate.convertAndSend("s.exchange.direct.1", "s.direct.route.1", "此消息发往直连交换机", new CorrelationData());
    }

    @Override
    public void writeMessageHeaders() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("header1", 1);
        messageProperties.setHeader("header2", 3);

        Message message = new Message("此消息发往头部交换机".getBytes(), messageProperties);
        sRabbitTemplate.convertAndSend("s.exchange.headers", "", message, new CorrelationData());
    }

    @Override
    public void writeMessageFanout() {
        sRabbitTemplate.convertAndSend("s.exchange.fanout", "", "此消息发往扇形交换机", new CorrelationData());
    }

    @Transactional(rollbackFor = Exception.class)
    public void writeMessageTransacted() {
        // 开启事务
        sRabbitTemplate.setChannelTransacted(true);
        sRabbitTemplate.convertAndSend("s.queue.test");
    }
}
