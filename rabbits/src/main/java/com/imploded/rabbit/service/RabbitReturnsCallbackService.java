package com.imploded.rabbit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 只有当交换机向队列（exchange->queue）写入失败时, 此类的回调方法会触发. 否则不执行
 *
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
