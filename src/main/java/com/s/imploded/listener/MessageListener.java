package com.s.imploded.listener;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class MessageListener {

    /**
     * queues = "y.queue.test" 监听队列
     * concurrency= "2-4" 监听线程数
     */
    @RabbitListener(queues = "y.queue.test", concurrency = "2-4")
    public void acceptMessage(Message message, Channel channel, String msg) {
        log.info("接受到消息: {}", msg);
        MessageProperties messageProperties = message.getMessageProperties();
        messageProperties.getAppId();
        log.info("接受到消息: {}", JSONObject.toJSONString(message));
        log.info("接受到消息: {}", JSONObject.toJSONString(channel));
//        log.info("接受到消息: {}", JSONObject.toJSONString(exchange));, Exchange exchange
    }

    /**
     * Message 接受消息体和消息头
     */
    @RabbitListener(queues = "y.queue.test.2", concurrency = "2-4")
    public void acceptMessage(Message message) {

    }

    /**
     * 手动ACK
     * Channel 通道
     */
    @RabbitListener(queues = "y.queue.test.3", concurrency = "2-4")
    public void acceptMessageAck(Channel channel, Message message) {
        try {
            // 手动ACK
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
