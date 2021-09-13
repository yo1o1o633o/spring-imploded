package com.imploded.complex.service.kafka.impl;

import com.imploded.complex.service.kafka.ConfigService;
import com.imploded.complex.service.kafka.common.ConsumerRunning;
import com.imploded.complex.service.kafka.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author shuai.yang
 */
@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {
    @Autowired
    ConfigService configService;

    @Override
    public void receiveMessage() {
        // 初始化配置
        Properties properties = configService.initConsumerConfig();
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

    /**
     * 重置消费位移, 使消费者从指定的位移重新消费
     * */
    private void seekConsumer() {
        Properties properties = configService.initConsumerConfig();
        // 创建消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
        // 订阅主题
        consumer.subscribe(Collections.singleton("topic-1"));
        Set<TopicPartition> assignments = new HashSet<>();
        // 自旋, 重复尝试获取分区信息
        while (assignments.size() == 0) {
            // 调用poll使消费者分配分区信息, 100毫秒超时, 当poll()方法中的参数为0时,此方法立刻返回,那么poll())方法内部进行分区分配的逻辑就会来不及实施
            consumer.poll(Duration.ofMillis(100));
            // 消费者分配到的分区信息
            assignments = consumer.assignment();
        }
        Map<TopicPartition, Long> partitionLongMap = new HashMap<>();
        for (TopicPartition assignment : assignments) {
            partitionLongMap.put(assignment, System.currentTimeMillis() - 86400000);
        }
        Map<TopicPartition, OffsetAndTimestamp> offsetAndTimestampMap = consumer.offsetsForTimes(partitionLongMap);
        for (TopicPartition assignment : assignments) {
            OffsetAndTimestamp offsetAndTimestamp = offsetAndTimestampMap.get(assignment);
            if (offsetAndTimestamp != null) {
                consumer.seek(assignment, offsetAndTimestamp.offset());
            }
            // seek方法重置该分区的消费位移
            consumer.seek(assignment, 10);
        }
        // 重置该分区消费位移到分区头部, 头部不一定是0, 因为日志清理的动作会清理旧的数据, 所以分区的起始位置会自然而然地增加
        consumer.seekToBeginning(assignments);
        // 重置该分区消费位移到分区尾部
        consumer.seekToEnd(assignments);
        // 消费逻辑......
    }

    private void rebalanceConsumer() {
        // 保存消费位移
        Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
        Properties properties = configService.initConsumerConfig();
        // 创建消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
        // 订阅主题并添加再均衡监听器
        consumer.subscribe(Collections.singleton("topic-1"), new ConsumerRebalanceListener() {
            // 当发生再均衡操作
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                // 发生再均衡, 提交消费位移, 避免重复消费
                consumer.commitSync(currentOffsets);
                currentOffsets.clear();
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            }
        });
        // 拉取消息
        ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<Object, Object> record : records) {
            currentOffsets.put(new TopicPartition(record.topic(), record.partition()), new OffsetAndMetadata(record.offset() + 1));
        }
        // 正常消费的异步提交位移
        consumer.commitAsync(currentOffsets, null);
    }

    /**
     * 多线程消费
     * */
    private void multiThreadConsumer() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,
                3,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Properties properties = configService.initConsumerConfig();

        for (int i = 0; i < 3; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    // 创建消费者
                    KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
                    // 订阅主题
                    consumer.subscribe(Collections.singleton("topic-1"));
                    try {
                        while (true) {
                            // 拉取消息
                            ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofMillis(100));
                            // 处理消息
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        consumer.close();
                    }
                }
            });
        }
    }

    private final static Map<TopicPartition, OffsetAndMetadata> OFFSETS = new HashMap<>();

    /**
     * 多线程消费
     * */
    private void multiThreadConsumerProcess() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,
                5,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        Properties properties = configService.initConsumerConfig();
        // 创建消费者
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<>(properties);
        // 订阅主题
        consumer.subscribe(Collections.singleton("topic-1"));
        // 拉取消息
        ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofMillis(100));
        // 每拉取到消息就发生给线程池处理
        executor.submit(() -> processMessage(records, consumer));
    }

    private void processMessage(ConsumerRecords<Object, Object> records, KafkaConsumer<Object, Object> consumer) {
        // 提交位移
        synchronized (OFFSETS) {
            if (!OFFSETS.isEmpty()) {
                consumer.commitSync(OFFSETS);
                OFFSETS.clear();
            }
        }
        // 处理消息
        for (TopicPartition partition : records.partitions()) {
            List<ConsumerRecord<Object, Object>> recordList = records.records(partition);
            long offset = recordList.get(recordList.size() - 1).offset();
            // 对公共位移加锁
            synchronized (OFFSETS) {
                // 如果不存在, 直接保存位移, 否则取出位移判断
                if (OFFSETS.containsKey(partition)) {
                    OFFSETS.put(partition, new OffsetAndMetadata(offset + 1));
                } else {
                    long p = OFFSETS.get(partition).offset();
                    if (p < offset + 1) {
                        OFFSETS.put(partition, new OffsetAndMetadata(offset + 1));
                    }
                }
            }
        }
    }
}
