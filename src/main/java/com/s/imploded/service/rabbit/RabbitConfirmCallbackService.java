package com.s.imploded.service.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 消息写入确认类，该类实现RabbitTemplate.ConfirmCallback, 实现的confirm接口回调参数中ack会标记写入broker成功状态, 保证消息成功写入broker中
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
