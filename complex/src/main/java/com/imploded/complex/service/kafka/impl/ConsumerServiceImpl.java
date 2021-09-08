package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.ConsumerRunning;
import com.imploded.complex.service.kafka.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void receiveMessage() {
        // 初始化配置
        Properties properties = initConfig();
        // 创建一个消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
        // 订阅主题
        consumer.subscribe(Collections.singleton("topic-1"));
        // 拉取一批消息
        ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(Long.MAX_VALUE));
        // 获取拉取到的消息集中的所有分区
        Set<TopicPartition> partitions = records.partitions();
        // 遍历分区
        for (TopicPartition partition : partitions) {
            // 根据分区号在消息集中获取消息列表
            List<ConsumerRecord<Object, Object>> recordList = records.records(partition);
            for (ConsumerRecord<Object, Object> record : recordList) {
                log.info("消费消息: {}", record.value());
            }
            // 最后一条消息的位移
            long offset = recordList.get(recordList.size() - 1).offset();
            // 同步提交, 按分区粒度同步提交消费位移
            consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)));
            // 异步提交
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if (exception == null) {
                        log.info("提交位移成功: {}", offsets);
                    } else {
                        log.error("提交位移失败: {}", offsets, exception);
                    }
                }
            });
        }
    }

    private Properties initConfig() {
        Properties properties = new Properties();
        // 连接Kafka集群的Broker地址列表
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9001");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "");
        // 相对生产者特有的参数, 消费组名称. 不能为空
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    /**
     * 取消订阅
     * */
    private void unsubscribe(KafkaConsumer<Object, Object> consumer) {
        // 取消订阅方法
        consumer.unsubscribe();
        // 订阅空主题, 也相当于取消订阅操作
        consumer.subscribe(new ArrayList<>());
    }

    /**
     * 可关闭的消费逻辑
     * */
    private void consumeMessage(KafkaConsumer<Object, Object> consumer) {
        try {
            while (ConsumerRunning.getRunning()) {
                ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(Long.MAX_VALUE));
                // 获取拉取到的消息集中的所有分区
                Set<TopicPartition> partitions = records.partitions();
                // 遍历分区
                for (TopicPartition partition : partitions) {
                    // 处理消息
                    Long offset = processMessage(records, partition);
                    // 提交位移
                    consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(offset + 1)));
                }
            }
        } catch (WakeupException e) {
            log.info("监听WakeupException异常, 其他线程调用wakeup方法...");
        } catch (Exception e) {
            log.error("其他异常");
        } finally {
            // 关闭消费者资源, 可能会触发提交位移操作
            consumer.close();
        }
    }

    /**
     * 对状态设置成false, 可以关闭上个消费方法
     * */
    private void closeConsumer() {
        ConsumerRunning.setRunning(false);
    }

    /**
     * 调用wakeup()方法, 可以关闭上个消费方法
     * */
    private void closeConsumerWithWakeUp(KafkaConsumer<Object, Object> consumer) {
        consumer.wakeup();
    }

    private Long processMessage(ConsumerRecords<Object, Object> records, TopicPartition partition) {
        // 根据分区号在消息集中获取消息列表
        List<ConsumerRecord<Object, Object>> recordList = records.records(partition);
        for (ConsumerRecord<Object, Object> record : recordList) {
            log.info("消费消息: {}", record.value());
        }
        // 最后一条消息的位移
        return recordList.get(recordList.size() - 1).offset();
    }
}
