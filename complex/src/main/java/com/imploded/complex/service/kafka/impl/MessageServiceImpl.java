package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.MessageService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shuai.yang
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public ProducerRecord<String, String> makeMessage() {
        return makeMessageWithTopicAndValue();
    }

    @Override
    public void receiveMessage() {

    }

    /**
     * 指定主题和消息内容创建消息, 其他参数取默认值
     */
    private ProducerRecord<String, String> makeMessageWithTopicAndValue() {
        return new ProducerRecord<>("topic-1", "Hello World");
    }

    /**
     * 指定主题,消息KEY和消息内容创建消息, 其他参数取默认值
     */
    private ProducerRecord<String, String> makeMessageWithTopicAndKeyAndValue() {
        return new ProducerRecord<>("topic-1", "key-1", "Hello World");
    }

    /**
     * 指定主题,消息写入分区号,消息KEY和消息内容创建消息, 其他参数取默认值
     */
    private ProducerRecord<String, String> makeMessageWithTopicAndPartitionAndKeyAndValue() {
        return new ProducerRecord<>("topic-1", 1, "key-1", "Hello World");
    }

    /**
     * 指定主题,消息写入分区号,消息KEY,消息内容和自定义消息请求头信息创建消息, 其他参数取默认值
     */
    private ProducerRecord<String, String> makeMessageWithTopicAndPartitionAndKeyAndValueAndHeaders() {
        return new ProducerRecord<>("topic-1", 1, "key-1", "Hello World", new ArrayList<>());
    }

    /**
     * 指定主题,消息写入分区号,消息过期时间, 消息KEY,消息内容和自定义消息请求头信息创建消息
     */
    private ProducerRecord<String, String> makeMessageWithAllParameter() {
        return new ProducerRecord<>("topic-1", 1, 10L, "key-1", "Hello World", new ArrayList<>());
    }
}
