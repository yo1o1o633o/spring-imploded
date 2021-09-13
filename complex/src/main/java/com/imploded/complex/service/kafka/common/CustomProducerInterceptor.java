package com.imploded.complex.service.kafka.common;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 生产者拦截器
 * @author shuai.yang
 */
public class CustomProducerInterceptor implements ProducerInterceptor {

    /**
     * 此方法会在消息发送之前执行, 对消息进行一些处理
     * KEY不要随意修改, 有可能影响日志压缩等功能
     * */
    @Override
    public ProducerRecord onSend(ProducerRecord record) {
        return null;
    }

    /**
     * 消息应答之前或消息发生失败时执行, 同时在自定义的回调函数之前执行
     * 此方法运行在Producer的I/O线程中, 尽量简单, 会影响消息发生速度
     * */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
