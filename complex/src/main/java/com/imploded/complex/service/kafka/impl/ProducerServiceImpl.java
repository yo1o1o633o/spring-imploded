package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.CustomPartitioner;
import com.imploded.complex.service.kafka.CustomProducerInterceptor;
import com.imploded.complex.service.kafka.ProducerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private KafkaProducer<String, String> kafkaProducer;

    @Override
    public void sendMessage() {
        // 初始化配置参数
        Properties properties = initConfig();
        // 创建生产者实例, KafkaProducer是线程安全的
        this.kafkaProducer = new KafkaProducer<>(properties);
        // 创建一条消息
        ProducerRecord<String, String> record = initMessage();
        // 发后即忘
        kafkaProducer.send(record);
        // 同步发送(Sync)
        syncSendMessage(record);
        // 同步发送(Sync), 获取返回值
        RecordMetadata recordMetadata = syncSendMessageResult(record);
        log.info("同步发送消息, 返回元数据信息: {}", recordMetadata);
        // 异步发送
        asyncSendMessage(record);
        // 关闭生产者实例, 回收资源
        kafkaProducer.close();
    }

    /**
     * 同步发送
     * */
    private void syncSendMessage(ProducerRecord<String, String> record) {
        // 同步发送(Sync), 可靠性高, 可捕获发生过程中的异常, 性能较差
        try {
            kafkaProducer.send(record).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步发送, 阻塞获取返回的元数据
     * */
    private RecordMetadata syncSendMessageResult(ProducerRecord<String, String> record) {
        // 获取发送元数据信息, 返回一个Future对象, 支持Future的特性, 如超时获取
        Future<RecordMetadata> future = kafkaProducer.send(record);
        try {
            RecordMetadata metadata = future.get();
            // 指定超时时间的获取
            RecordMetadata metadataTimeOut = future.get(1000, TimeUnit.SECONDS);
            return metadata;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步发送, 增加回调函数处理发送状态
     * */
    private void asyncSendMessage(ProducerRecord<String, String> record) {
        // 异步发送(Async), 增加回调函数在消息发送之后回调发送状态, 成功或者异常. 异步回调也可以保证分区有序性
        kafkaProducer.send(record, new Callback() {
            // RecordMetadata和Exception互斥, 成功则Exception为NULL, RecordMetadata不为NULL, 异常则Exception不为NULL, RecordMetadata为NULL
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                    System.out.println("消息发送成功");
                }
            }
        });
    }

    private Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "");
        // 重试次数, 对于可重试异常, 在此配置重复次数内自行恢复就不会抛出异常, 默认0
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        // 默认序列化器, 可以自定义, 生产者和消费者的序列化器要保持一致
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 自定义分区器
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());
        // 自定义拦截器
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomProducerInterceptor.class.getName());
        return properties;
    }

    private ProducerRecord<String, String> initMessage() {
        // 消息头
        List<Header> headers = new ArrayList<>();
        // 指定消息发往的主题
        String topic = "topic-1";
        // 指定消息发往的分区
        Integer partition = 1;
        // 时间戳
        Long timestamp = 100L;
        // 消息KEY
        String key = "key-one";
        // 消息内容
        String value = "Hello World!";
        // 多个构造方法, 其他构造方法内部都是调用此构造方法, 其他入参不传按NULL处理
        return new ProducerRecord<>(topic, partition, timestamp, key, value, headers);
    }
}
