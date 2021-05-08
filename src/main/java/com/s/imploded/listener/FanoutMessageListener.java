package com.s.imploded.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class FanoutMessageListener {

    /**
     * concurrency= "2-4" 监听线程数
     */
    @RabbitListener(queues = "s.queue.fanout.1", concurrency = "2-4")
    public void receive1(Message message, Channel channel, String msg) {
        log.info("接受扇形交换机消息, 消息队列: {}, 消息体: {}, 通道: {}, 内容: {}", "s.queue.fanout.1", message, channel, msg);
    }

    /**
     * concurrency= "2-4" 监听线程数
     */
    @RabbitListener(queues = "s.queue.fanout.2", concurrency = "2-4")
    public void receive2(Message message, Channel channel, String msg) {
        log.info("接受扇形交换机消息, 消息队列: {}, 消息体: {}, 通道: {}, 内容: {}", "s.queue.fanout.2", message, channel, msg);
    }

    /**
     * concurrency= "2-4" 监听线程数
     */
    @RabbitListener(queues = "s.queue.fanout.3", concurrency = "2-4")
    public void receive3(Message message, Channel channel, String msg) {
        log.info("接受扇形交换机消息, 消息队列: {}, 消息体: {}, 通道: {}, 内容: {}", "s.queue.fanout.3", message, channel, msg);
    }
}
