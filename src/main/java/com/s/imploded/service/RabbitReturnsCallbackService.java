package com.s.imploded.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author shuai.yang
 */
@Slf4j
public class RabbitReturnsCallbackService implements RabbitTemplate.ReturnsCallback {

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        String routingKey = returned.getRoutingKey();
        String exchange = returned.getExchange();
        log.info("routingKey: {}, exchange: {}", routingKey, exchange);

        int replyCode = returned.getReplyCode();
        String replyText = returned.getReplyText();
        log.info("replyCode: {}, replyText: {}", replyCode, replyText);

        Message message = returned.getMessage();
        log.info("messageProperties: {}, body: {}", message.getMessageProperties(), message.getBody());
    }
}
