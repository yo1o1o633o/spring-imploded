package com.s.imploded.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author shuai.yang
 */
@Slf4j
public class RabbitConfirmCallbackService implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId();
        if (!ack) {
            log.info("消息发送失败: {}", cause);
        } else {
            log.info("写入成功消息ID: {}", id);
        }
    }
}
