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

    /**
     * 写主题消息
     * */
    @Override
    public void writeMessageTopic() {
        sRabbitTemplate.convertAndSend("s.exchange.topic", "topic.1", "此消息发往主题交换机1", new CorrelationData());
        sRabbitTemplate.convertAndSend("s.exchange.topic", "topic.2", "此消息发往主题交换机2", new CorrelationData());
    }

    /**
     * 写直连消息
     * */
    @Override
    public void writeMessageDirect() {
        sRabbitTemplate.convertAndSend("s.exchange.direct.1", "s.direct.route.1", "此消息发往直连交换机", new CorrelationData());
    }

    /**
     * 写头部消息
     * */
    @Override
    public void writeMessageHeaders() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("header1", 1);
        messageProperties.setHeader("header2", 3);

        Message message = new Message("此消息发往头部交换机".getBytes(), messageProperties);
        sRabbitTemplate.convertAndSend("s.exchange.headers", "", message, new CorrelationData());
    }

    /**
     * 写扇形消息
     * */
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
