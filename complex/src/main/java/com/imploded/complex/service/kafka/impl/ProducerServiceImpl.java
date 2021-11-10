package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.ConfigService;
import com.imploded.complex.service.kafka.MessageService;
import com.imploded.complex.service.kafka.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class ProducerServiceImpl implements ProducerService {
    @Autowired
    ConfigService configService;
    @Autowired
    MessageService messageService;

    private KafkaProducer<String, String> kafkaProducer;

    private void initProducer() {
        // 初始化配置参数
        Properties properties = configService.initProducerConfig();
        // 创建生产者实例, KafkaProducer是线程安全的
        if (kafkaProducer == null) {
            this.kafkaProducer = new KafkaProducer<>(properties);
        }
    }

    @Override
    public void sendMessage() {
        initProducer();
        // 创建一条消息
        ProducerRecord<String, String> record = messageService.makeMessage();
        // 发后即忘
        kafkaProducer.send(record);
        // 关闭生产者实例, 回收资源
        kafkaProducer.close();
    }

    /**
     * 同步发送
     * */
    @Override
    public void syncSendMessage() {
        // 创建一条消息
        ProducerRecord<String, String> record = messageService.makeMessage();
        // 同步发送(Sync), 可靠性高, 可捕获发生过程中的异常, 性能较差
        try {
            kafkaProducer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 关闭生产者实例, 回收资源
        kafkaProducer.close();
    }

    /**
     * 同步发送, 阻塞获取返回的元数据
     * */
    @Override
    public RecordMetadata syncSendMessageResult() {
        // 创建一条消息
        ProducerRecord<String, String> record = messageService.makeMessage();
        // 获取发送元数据信息, 返回一个Future对象, 支持Future的特性, 如超时获取
        Future<RecordMetadata> future = kafkaProducer.send(record);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 关闭生产者实例, 回收资源
        kafkaProducer.close();
        return null;
    }

    /**
     * 同步发送, 阻塞获取返回的元数据(指定阻塞超时时间)
     * */
    @Override
    public RecordMetadata syncSendMessageResultTimeOut() {
        // 创建一条消息
        ProducerRecord<String, String> record = messageService.makeMessage();
        // 获取发送元数据信息, 返回一个Future对象, 支持Future的特性, 如超时获取
        Future<RecordMetadata> future = kafkaProducer.send(record);
        try {
            // 指定超时时间的获取
            return future.get(1000, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        // 关闭生产者实例, 回收资源
        kafkaProducer.close();
        return null;
    }

    /**
     * 异步发送, 增加回调函数处理发送状态, new Callback()
     * */
    @Override
    public void asyncSendMessage() {
        // 创建一条消息
        ProducerRecord<String, String> record = messageService.makeMessage();
        // 异步发送(Async), 增加回调函数在消息发送之后回调发送状态, 成功或者异常. 异步回调也可以保证分区有序性
        // RecordMetadata和Exception互斥, 成功则Exception为NULL, RecordMetadata不为NULL, 异常则Exception不为NULL, RecordMetadata为NULL
        kafkaProducer.send(record, (metadata, exception) -> {
            if (exception != null) {
                exception.printStackTrace();
            } else {
                System.out.println("消息发送成功");
            }
        });
        // 关闭生产者实例, 回收资源
        kafkaProducer.close();
    }
}
